package com.broker.scheduler.service.v3;

import com.broker.scheduler.service.v3.model.AlreadyScheduled;
import com.broker.scheduler.service.v3.model.RandomNumber;
import com.broker.scheduler.service.v3.model.Schedule;
import com.broker.scheduler.service.v3.score.CalculateScore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class IterationManager {

    private static final int NUMBERS_OF_ITERATION = 5000;
    private final CalculateScore calculateScore;
    private final ScheduleBuilder scheduleBuilder;
    private final AlreadyScheduled alreadyScheduled;
    private final RandomNumber randomNumber;

    public IterationManager() {
        this.calculateScore = new CalculateScore();
        this.scheduleBuilder = new ScheduleBuilder();
        this.alreadyScheduled = new AlreadyScheduled();
        this.randomNumber = new RandomNumber();
    }

    public Schedule iterate(Schedule schedule) throws IOException {

        Schedule lowerScore = null;

        int count = 0;
        while (NUMBERS_OF_ITERATION > count) {

            schedule = scheduleBuilder.createSchedule(schedule, alreadyScheduled, randomNumber);

            Schedule scoredSchedule = calculateScore.calculate(schedule);
            if (lowerScore == null) {
                lowerScore = cloneSchedule(scoredSchedule);
            }
            log.info("Scored Lower: {}", lowerScore.getScore().toString());
            log.info("Scored Calculated: {}", scoredSchedule.getScore().toString());
            if (lowerScore.getScore().intValue() > scoredSchedule.getScore().intValue()) {
                lowerScore = cloneSchedule(scoredSchedule);
            } else {
                double threshold = ThresholdCalculator.average(scoredSchedule.getBrokerV3s());
                //TODO: Clear Broker Score when removing it
                List<Schedule.BrokerV3> brokerV3s = scoredSchedule.removeAllBrokersForThreshold(threshold);

                alreadyScheduled.removeBrokers(brokerV3s);
            }
            count++;
        }
        return lowerScore;
    }

    private Schedule cloneSchedule(Schedule scoredSchedule) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String scheduleJson = objectMapper.writeValueAsString(scoredSchedule);
        return objectMapper.readValue(scheduleJson, Schedule.class);
    }
}
