package com.broker.scheduler.service.v2.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "schedulerV2")
public class ScheduleModelV2 {
    @Id
    private String id;
    private List<Plantao> plantaos;
}
