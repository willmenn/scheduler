package com.broker.scheduler.service.v3;

import com.broker.scheduler.client.BrokerClient;
import com.broker.scheduler.client.ShiftPlaceClient;
import com.broker.scheduler.model.Broker;
import com.broker.scheduler.service.v2.model.Plantao;
import com.broker.scheduler.service.v3.model.Schedule;
import com.broker.scheduler.service.v3.model.Schedules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceV3 {

    private BrokerClient brokerClient;
    private ShiftPlaceClient shiftPlaceClient;

    @Autowired
    public ScheduleServiceV3(BrokerClient brokerClient, ShiftPlaceClient shiftPlaceClient) {
        this.brokerClient = brokerClient;
        this.shiftPlaceClient = shiftPlaceClient;
    }

    public Schedules createSchedules(String manager) {
        List<Broker> brokers = brokerClient.fetchBrokersByManager(manager);
        List<Plantao> shiftPlaces = shiftPlaceClient.fetchShiftPlaceByManagerV2(manager);

        Schedule schedule = new Schedule().convertShiftPlaceToSchedule(shiftPlaces).setBrokers(brokers);

        return null;
    }


}
