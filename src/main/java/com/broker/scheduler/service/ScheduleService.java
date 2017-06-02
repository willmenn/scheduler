package com.broker.scheduler.service;

import com.broker.scheduler.client.BrokerClient;
import com.broker.scheduler.controller.ScheduleController;
import com.broker.scheduler.model.Broker;
import com.broker.scheduler.model.ScheduleModel;
import com.broker.scheduler.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    private ScheduleRepository repository;
    private BrokerClient brokerClient;

    @Autowired
    public ScheduleService(ScheduleRepository repository, BrokerClient brokerClient) {
        this.repository = repository;
        this.brokerClient = brokerClient;
    }

    public ScheduleModel createSchedule(ScheduleController.ScheduleDTO dto) {
        List<Broker> brokers = brokerClient.fetchBrokersByManager(dto.getManager());
        return repository.save(ScheduleModel.builder()
                .manager(dto.getManager())
                .brokers(brokers)
                .build());
    }

    public ScheduleModel fetchSchduelById(String id) {
        return repository.findOne(id);
    }
}
