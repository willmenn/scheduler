package com.broker.scheduler.service.v3;

import com.broker.scheduler.service.v3.model.Schedule;


import java.util.List;
import java.util.stream.IntStream;

public class ThresholdCalculator {

    public static Double average(List<Schedule.BrokerV3> brokerV3s) {
        return brokerV3s.stream()
                .mapToInt(b -> b.getScore().intValue())
                .summaryStatistics().getAverage();
    }

    public static Double standarDeviation(List<Schedule.BrokerV3> brokerV3s) {
        int sum = brokerV3s.stream()
                .mapToInt(b -> b.getScore().intValue()).sum();

        double variance = sum / brokerV3s.size();
        return Math.sqrt(variance);
    }
}
