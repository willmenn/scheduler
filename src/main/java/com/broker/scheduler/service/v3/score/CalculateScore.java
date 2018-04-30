package com.broker.scheduler.service.v3.score;

import com.broker.scheduler.service.v3.model.Schedule;

import java.util.Map;

/**
 * Created by wahrons on 30/04/18.
 */
public class CalculateScore {

    public Schedule calculate(Schedule schedule) {

        Map<String, BrokerScore> brokerScore = new BrokerScore().buildBrokerScoreMap(schedule);

        return null;
    }




}
