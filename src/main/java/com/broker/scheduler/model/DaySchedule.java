package com.broker.scheduler.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@AllArgsConstructor
@Getter
public class DaySchedule {
    private String placeId;
    @NonNull
    private String day;
    private List<Broker> brokers;
}
