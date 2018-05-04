package com.broker.scheduler.service.v3;

import com.broker.scheduler.service.v3.model.Schedule;

import java.util.List;

public class ThresholdCalculator {

    public static Double average(List<Schedule.BrokerV3> brokerV3s){
        return brokerV3s.stream()
                .mapToInt(b -> b.getScore().intValue())
                .summaryStatistics().getAverage();
    }
}
