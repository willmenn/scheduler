package com.broker.scheduler.service.v3;

import com.broker.scheduler.client.BrokerClient;
import com.broker.scheduler.client.ShiftPlaceClient;
import com.broker.scheduler.model.Broker;
import com.broker.scheduler.service.v2.model.Plantao;
import com.broker.scheduler.service.v3.model.AlreadyScheduled;
import com.broker.scheduler.service.v3.model.RandomNumber;
import com.broker.scheduler.service.v3.model.Schedule;
import com.broker.scheduler.service.v3.model.Schedules;
import com.broker.scheduler.service.v3.score.CalculateScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleServiceV3 {

    private BrokerClient brokerClient;
    private ShiftPlaceClient shiftPlaceClient;
    private IterationManager iterationManager;

    @Autowired
    public ScheduleServiceV3(BrokerClient brokerClient, ShiftPlaceClient shiftPlaceClient,
                             IterationManager iterationManager) {
        this.brokerClient = brokerClient;
        this.shiftPlaceClient = shiftPlaceClient;
        this.iterationManager = iterationManager;
    }

    public Schedules createSchedules(String manager) {
        List<Broker> brokers = brokerClient.fetchBrokersByManager(manager);
        List<Plantao> shiftPlaces = shiftPlaceClient.fetchShiftPlaceByManagerV2(manager);

        Schedule schedule = new Schedule().convertShiftPlaceToSchedule(shiftPlaces).setBrokers(brokers);

        try {
            schedule = iterationManager.iterate(schedule);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Schedules schedules = new Schedules();
        schedules.setSchedule(new ArrayList<>());
        schedules.getSchedule().add(schedule);
        return schedules;
    }


}
