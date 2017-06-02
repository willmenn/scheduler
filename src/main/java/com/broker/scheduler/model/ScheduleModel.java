package com.broker.scheduler.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class ScheduleModel {
    @Id
    private String id;
    private String manager;
}
