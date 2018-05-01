package com.broker.scheduler.service.v3.score;

import com.broker.scheduler.service.v3.model.Schedule;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by wahrons on 30/04/18.
 */
public class CalculateScore {

    public Schedule calculate(Schedule schedule) {

        Map<String, BrokerScore> brokerScoreMap = new BrokerScore().buildBrokerScoreMap(schedule);

        int sum = brokerScoreMap.values().stream()
                .mapToInt(BrokerScore::calculateScore)
                .sum();

        schedule.setScore(BigDecimal.valueOf(sum));
        return schedule;
    }
}
