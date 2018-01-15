package com.broker.scheduler.service.v2;

import com.broker.scheduler.client.BrokerClient;
import com.broker.scheduler.client.ShiftPlaceClient;
import com.broker.scheduler.model.Broker;
import com.broker.scheduler.repository.ScheduleV2Repository;
import com.broker.scheduler.service.v2.BuildMultiSchedule.ScheduleWrapper;
import com.broker.scheduler.service.v2.model.Plantao;
import com.broker.scheduler.service.v2.model.ScheduleModelV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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


    public List<Plantao> buildSchedule(String managerName) {
        List<Broker> brokers = brokerClient.fetchBrokersByManagerV2(managerName);
        List<Plantao> plantoes = shiftPlaceClient.fetchShiftPlaceByManagerV2(managerName);

        ScheduleWrapper scheduleWrapper = buildMultiSchedule.build(plantoes, brokers);
        List<Plantao> plantaos = filBlankSpace
                .toFilBlankSpace(scheduleWrapper.getPlantaos(), scheduleWrapper.alreadyScheduled, brokers);

        repository.save(ScheduleModelV2.builder()
                .plantaos(plantaos)
                .build());

        return plantaos;
    }

    public ScheduleModelV2 getScheduleV2(String id){
        return repository.findOne(id);
    }
}
