package com.broker.scheduler.service;

import com.broker.scheduler.client.BrokerClient;
import com.broker.scheduler.client.ShifPlaceClient;
import com.broker.scheduler.controller.ScheduleController;
import com.broker.scheduler.model.Broker;
import com.broker.scheduler.model.ScheduleModel;
import com.broker.scheduler.model.ShiftPlace;
import com.broker.scheduler.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    private ScheduleRepository repository;
    private BrokerClient brokerClient;
    private ShifPlaceClient shifPlaceClient;

    @Autowired
    public ScheduleService(ScheduleRepository repository,
                           BrokerClient brokerClient,
                           ShifPlaceClient shifPlaceClient) {
        this.repository = repository;
        this.brokerClient = brokerClient;
        this.shifPlaceClient = shifPlaceClient;
    }

    public ScheduleModel createSchedule(ScheduleController.ScheduleDTO dto) {
        List<Broker> brokers = brokerClient.fetchBrokersByManager(dto.getManager());
        List<ShiftPlace> shiftPlaces = shifPlaceClient.fetchShiftPlaceByManager(dto.getManager());
        return repository.save(ScheduleModel.builder()
                .manager(dto.getManager())
                .brokers(brokers)
                .shiftPlaces(shiftPlaces)
                .build());
    }

    public ScheduleModel fetchSchduelById(String id) {
        return repository.findOne(id);
    }
}
