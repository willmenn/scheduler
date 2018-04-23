package com.broker.scheduler.service.v2;

import com.broker.scheduler.client.BrokerClient;
import com.broker.scheduler.client.ShiftPlaceClient;
import com.broker.scheduler.model.Broker;
import com.broker.scheduler.repository.ScheduleV2Repository;
import com.broker.scheduler.service.v2.BuildMultiSchedule.ScheduleWrapper;
import com.broker.scheduler.service.v2.model.Plantao;
import com.broker.scheduler.service.v2.model.ScheduleModelV2;
import com.broker.scheduler.service.v2.model.ShiftPlaceDay;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

/**
 * Created by wahrons on 15/01/18.
 */
@Service
public class ScheduleBrokerServiceV2 {

    private BuildMultiSchedule buildMultiSchedule;
    private FilBlankSpace filBlankSpace;
    private BrokerClient brokerClient;
    private ShiftPlaceClient shiftPlaceClient;
    private ScheduleV2Repository repository;

    @Autowired
    public ScheduleBrokerServiceV2(BuildMultiSchedule buildMultiSchedule, FilBlankSpace filBlankSpace,
                                   BrokerClient brokerClient, ShiftPlaceClient shiftPlaceClient,
                                   ScheduleV2Repository repository) {
        this.buildMultiSchedule = buildMultiSchedule;
        this.filBlankSpace = filBlankSpace;
        this.brokerClient = brokerClient;
        this.shiftPlaceClient = shiftPlaceClient;
        this.repository = repository;
    }


    public ScheduleModelV2 buildSchedule(String managerName) {
        List<Broker> brokers = brokerClient.fetchBrokersByManager(managerName);
        Preconditions.checkArgument(brokers.size() > 0);
        List<Plantao> plantoes = shiftPlaceClient.fetchShiftPlaceByManagerV2(managerName);

        ScheduleWrapper scheduleWrapper = buildMultiSchedule.build(plantoes, brokers);
        List<Plantao> plantaos = filBlankSpace
                .toFilBlankSpace(scheduleWrapper.getPlantaos(), scheduleWrapper.alreadyScheduled, brokers);

        return repository.save(ScheduleModelV2.builder()
                .plantaos(plantaos)
                .managerName(managerName)
                .createdTimestamp(LocalDateTime.now(Clock.system(ZoneId.of("America/Sao_Paulo"))))
                .build());
    }

    public ScheduleModelV2 getScheduleV2(String id) {
        return repository.findOne(id);
    }

    public Map<Broker, List<ShiftPlaceDay>> getScheduleBrokerV2(String id) {
        ScheduleModelV2 scheduleV2 = getScheduleV2(id);
        List<Broker> brokers = brokerClient.fetchBrokersByManager(scheduleV2.getManagerName());
        Map<Broker, List<ShiftPlaceDay>> brokersSchedule = brokers.stream().collect(toMap(Function.identity(),
                s -> new ArrayList<ShiftPlaceDay>()));

        scheduleV2.getPlantaos()
                .forEach(p -> p.getScheduled().keySet()
                        .forEach(day -> p.getScheduled().get(day)
                                .forEach(broker -> brokersSchedule.get(broker)
                                        .add(new ShiftPlaceDay(p.getName(), p.getShiftPlaceId(), day)))));

        return brokersSchedule;
    }

    public List<ScheduleModelV2> getListScheduleV2(String managerName) {
        return repository.findByManagerNameOrderByCreatedTimestamp(managerName, new PageRequest(0, 10));
    }
}
