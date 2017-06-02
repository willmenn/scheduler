package com.broker.scheduler.service;

import com.broker.scheduler.controller.ScheduleController;
import com.broker.scheduler.model.ScheduleModel;
import com.broker.scheduler.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

    private ScheduleRepository repository;

    @Autowired
    public ScheduleService(ScheduleRepository repository) {
        this.repository = repository;
    }

    public ScheduleModel createSchedule(ScheduleController.ScheduleDTO dto) {
        return repository.save(ScheduleModel.builder().manager(dto.getManager()).build());
    }

    public ScheduleModel fetchSchduelById(String id) {
        return repository.findOne(id);
    }
}
