package com.broker.scheduler.service.v3;

import com.broker.scheduler.client.BrokerClient;
import com.broker.scheduler.client.ShiftPlaceClient;
import com.broker.scheduler.model.Broker;
import com.broker.scheduler.repository.ScheduleV3Repository;
import com.broker.scheduler.service.v2.model.Plantao;
import com.broker.scheduler.service.v3.model.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class ScheduleServiceV3 {

    private BrokerClient brokerClient;
    private ShiftPlaceClient shiftPlaceClient;
    private IterationManager iterationManager;
    private ScheduleV3Repository repository;

    @Autowired
    public ScheduleServiceV3(BrokerClient brokerClient, ShiftPlaceClient shiftPlaceClient,
                             IterationManager iterationManager, ScheduleV3Repository repository) {
        this.brokerClient = brokerClient;
        this.shiftPlaceClient = shiftPlaceClient;
        this.iterationManager = iterationManager;
        this.repository = repository;
    }

    public Schedule createSchedules(String manager) {
        List<Broker> brokers = brokerClient.fetchBrokersByManager(manager);
        List<Plantao> shiftPlaces = shiftPlaceClient.fetchShiftPlaceByManagerV2(manager);

        Schedule schedule = new Schedule().convertShiftPlaceToSchedule(shiftPlaces).setBrokers(brokers);

        try {
            schedule = iterationManager.iterate(schedule);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return repository.save(schedule.toBuilder().managerName(manager)
                .createdTimestamp(LocalDateTime.now(Clock.system(ZoneId.of("America/Sao_Paulo"))))
                .build());
    }


}
