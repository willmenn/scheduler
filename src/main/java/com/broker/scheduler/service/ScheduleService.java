package com.broker.scheduler.service;

import com.broker.scheduler.client.BrokerClient;
import com.broker.scheduler.client.ShifPlaceClient;
import com.broker.scheduler.controller.ScheduleController;
import com.broker.scheduler.model.Broker;
import com.broker.scheduler.model.ScheduleModel;
import com.broker.scheduler.model.ShiftPlace;
import com.broker.scheduler.model.WeekSchedule;
import com.broker.scheduler.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    private ScheduleRepository repository;
    private BrokerClient brokerClient;
    private ShifPlaceClient shifPlaceClient;
    private BuildWeekSchedule buildWeekSchedule;

    @Autowired
    public ScheduleService(ScheduleRepository repository,
                           BrokerClient brokerClient,
                           ShifPlaceClient shifPlaceClient,
                           BuildWeekSchedule buildWeekSchedule) {
        this.repository = repository;
        this.brokerClient = brokerClient;
        this.shifPlaceClient = shifPlaceClient;
        this.buildWeekSchedule = buildWeekSchedule;
    }

    public ScheduleModel createSchedule(ScheduleController.ScheduleDTO dto) {
        List<Broker> brokers = brokerClient.fetchBrokersByManager(dto.getManager());
        List<ShiftPlace> shiftPlaces = shifPlaceClient.fetchShiftPlaceByManager(dto.getManager());

        WeekSchedule weekSchedule = buildWeekSchedule.buildDaySchedule(brokers, shiftPlaces);

        return repository.save(ScheduleModel.builder()
                .manager(dto.getManager())
                .brokers(brokers)
                .shiftPlaces(shiftPlaces)
                .weekSchedule(weekSchedule)
                .build());
    }


    public ScheduleModel fetchSchduelById(String id) {
        return repository.findOne(id);
    }

    /*
    Cria um mapa por <Dia, list<Broker>> por preferencia.
    Cria um mapa por <Dia, list<ShiftPlace>>.
    Botar todos os brokers em uma pilha, e add conforme o numero de vagas no shiftplace;
     */
}
