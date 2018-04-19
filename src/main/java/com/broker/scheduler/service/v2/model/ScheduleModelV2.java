package com.broker.scheduler.service.v2.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Document(collection = "schedulerV2")
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleModelV2 {
    @Id
    private String id;
    private List<Plantao> plantaos;
    private String managerName;
    private LocalDateTime createdTimestamp;

    public String getTimestamp(){
        return this.createdTimestamp.toString();
    }
}
