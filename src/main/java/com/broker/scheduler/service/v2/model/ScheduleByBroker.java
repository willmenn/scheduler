package com.broker.scheduler.service.v2.model;

import com.broker.scheduler.model.Broker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Created by wahrons on 23/04/18.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScheduleByBroker {
    private Map<String, List<ShiftPlaceDay>> brokersSchedule;
    private List<Broker> brokers;
}
