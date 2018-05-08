package com.broker.scheduler.service.v3;

import com.broker.scheduler.service.v3.analytics.DumpLogToCsv;
import com.broker.scheduler.service.v3.model.AlreadyScheduled;
import com.broker.scheduler.service.v3.model.DayEnum;
import com.broker.scheduler.service.v3.model.FakeRandomNumber;
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
import java.util.Map;
import java.util.Optional;

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
            }
            //TODO: Clear Broker Score when removing it
            List<Schedule.BrokerV3> brokerV3s = scoredSchedule.removeAllBrokersForThreshold(threshold);

            alreadyScheduled.removeBrokers(brokerV3s);
            mutate(scoredSchedule, alreadyScheduled, randomNumber);
            count++;
        }
        dumpToFile("schedule");
        return lowerScore;
    }

    private void mutate(Schedule schedule, AlreadyScheduled alreadyScheduled, RandomNumber randomNumber) {
        boolean mutate = false;
        int count=0;
        while (count != 20) {
            Map<DayEnum, Schedule.Day> days = schedule.getShiftPlaceV3List()
                    .stream().findFirst().get().getDays();
            Optional<Map.Entry<DayEnum, Schedule.Day>> firstDay = days.entrySet().stream().findFirst();
            int randomInt = randomNumber.getRandomInt(3)+1;
            Schedule.Shift shift = null;
            log.info("" + randomInt);
            if (randomInt == 1) {
                shift = firstDay.get().getValue().getMorning();
            } else if (randomInt == 2) {
                shift = firstDay.get().getValue().getAfternoon();
            } else if (randomInt == 3) {
                shift = firstDay.get().getValue().getNight();
            }
            log.info(firstDay.toString());
            if (shift != null && shift.getBrokerV3List() != null && shift.getBrokerV3List().size() > 0) {
                log.info(shift.getName().name());
                Schedule.BrokerV3 removed = shift.getBrokerV3List()
                        .remove(randomNumber.getRandomInt(shift.getBrokerV3List().size()));
                mutate = alreadyScheduled.removeBrokerFromDayShift(removed, firstDay.get(), shift);
                log.info("MUTATE - " + mutate);
            }
            count++;
        }
        log.info("MUTATE - " + mutate);
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
                    scoredSchedule.getScore().intValue(), threshold);
        }
    }

    private Schedule cloneSchedule(Schedule scoredSchedule) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String scheduleJson = objectMapper.writeValueAsString(scoredSchedule);
        return objectMapper.readValue(scheduleJson, Schedule.class);
    }
}
