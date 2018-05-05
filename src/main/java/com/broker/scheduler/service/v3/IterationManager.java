package com.broker.scheduler.service.v3;

import com.broker.scheduler.service.v3.analytics.DumpLogToCsv;
import com.broker.scheduler.service.v3.model.AlreadyScheduled;
import com.broker.scheduler.service.v3.model.RandomNumber;
import com.broker.scheduler.service.v3.model.Schedule;
import com.broker.scheduler.service.v3.score.CalculateScore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private final DumpLogToCsv dump;
    private boolean dumpToLocalFile;

    @Autowired
    public IterationManager(@Value("${dumpToLocalFile:false}") boolean dumpToLocalFile) {
        this.dumpToLocalFile = dumpToLocalFile;
        this.calculateScore = new CalculateScore();
        this.scheduleBuilder = new ScheduleBuilder();
        this.alreadyScheduled = new AlreadyScheduled();
        this.randomNumber = new RandomNumber();
        this.dump = new DumpLogToCsv();
    }

    public Schedule iterate(Schedule schedule) throws IOException {

        Schedule lowerScore = null;
        clearDumpCollection();
        int count = 0;
        while (NUMBERS_OF_ITERATION > count) {

            schedule = scheduleBuilder.createSchedule(schedule, alreadyScheduled, randomNumber);

            Schedule scoredSchedule = calculateScore.calculate(schedule);
            if (lowerScore == null) {
                lowerScore = cloneSchedule(scoredSchedule);
            }
            double threshold = ThresholdCalculator.average(scoredSchedule.getBrokerV3s());
            logIteration(lowerScore, count, scoredSchedule, threshold);
            if (lowerScore.getScore().intValue() > scoredSchedule.getScore().intValue()) {
                lowerScore = cloneSchedule(scoredSchedule);
            } else {
                //TODO: Clear Broker Score when removing it
                List<Schedule.BrokerV3> brokerV3s = scoredSchedule.removeAllBrokersForThreshold(threshold);

                alreadyScheduled.removeBrokers(brokerV3s);
            }
            count++;
        }
        dumpToFile("schedule");
        return lowerScore;
    }

    private void clearDumpCollection() {
        if (this.dumpToLocalFile) {
            dump.clearCollection();
        }
    }

    private void dumpToFile(String scheduleId) {
        if (this.dumpToLocalFile) {
            this.dump.saveToFile(scheduleId);
        }
    }

    private void logIteration(Schedule lowerScore, int count, Schedule scoredSchedule, double threshold) {
        log.info("Scored Lower: {}", lowerScore.getScore().toString());
        log.info("Scored Calculated: {}", scoredSchedule.getScore().toString());
        if (this.dumpToLocalFile) {
            this.dump.addLine(count,
                    lowerScore.getScore().intValue(),
                    scoredSchedule.getScore().intValue(),threshold);
        }
    }

    private Schedule cloneSchedule(Schedule scoredSchedule) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String scheduleJson = objectMapper.writeValueAsString(scoredSchedule);
        return objectMapper.readValue(scheduleJson, Schedule.class);
    }
}
