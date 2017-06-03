package com.broker.scheduler.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Builder
public class ScheduleModel {
    @Id
    private String id;
    private String manager;
    private List<Broker> brokers;
    private List<ShiftPlace> shiftPlaces;
    private WeekSchedule weekSchedule;

}
