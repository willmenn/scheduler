package com.broker.scheduler.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Builder
public class ScheduleModel {
    @Id
    private String id;
    @NonNull
    private String manager;
    @NonNull
    private List<Broker> brokers;
    @NonNull
    private List<ShiftPlace> shiftPlaces;
}
