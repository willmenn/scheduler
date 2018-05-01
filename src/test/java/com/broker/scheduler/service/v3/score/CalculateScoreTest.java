package com.broker.scheduler.service.v3.score;

import com.broker.scheduler.service.v2.model.Plantao;
import com.broker.scheduler.service.v3.ScheduleBuilder;
import com.broker.scheduler.service.v3.model.AlreadyScheduled;
import com.broker.scheduler.service.v3.model.RandomNumber;
import com.broker.scheduler.service.v3.model.Schedule;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

/**
 * Created by wahrons on 30/04/18.
 */
public class CalculateScoreTest {
    private ScheduleBuilder builder;
    private CalculateScore calculateScore;

    @Before
    public void setUp() throws Exception {
        builder = new ScheduleBuilder();
        calculateScore = new CalculateScore();
    }

    @Test
    public void shouldBeAbleToCalculateScoreForSchedule() throws Exception {
        Map<String, Plantao.Shift> days = new HashMap<>();
        days.put("MON", new Plantao.Shift(3, 4, 5));

        String shiftPlaceName = "n-1";
        Plantao plantao = Plantao.builder().name(shiftPlaceName).daysV3(days).build();
        Schedule schedule = new Schedule().convertShiftPlaceToSchedule(newArrayList(plantao));

        Map<ScoreFunction, List<String>> constraints = new HashMap<>(1);
        constraints.put(ScoreFunction.PARTIAL_SHIFT, newArrayList("MORNING"));
        constraints.put(ScoreFunction.PARTIAL_SHIFT_PLACE, newArrayList("n-1"));
        constraints.put(ScoreFunction.PARTIAL_DAY, newArrayList("MON"));
        String brokerName = "John due";
        Schedule.BrokerV3 brokerV3 = new Schedule.BrokerV3(brokerName, "1", null, constraints);
        schedule.setBrokerV3s(newArrayList(brokerV3));
        AlreadyScheduled alreadyScheduled = new AlreadyScheduled();
        Schedule scheduleFilled = builder.createSchedule(schedule, alreadyScheduled, new RandomNumber());

        Schedule scheduledWithScore = calculateScore.calculate(scheduleFilled);

        assertEquals(3, scheduledWithScore.getScore().intValue());
    }

    @Test
    public void shouldBeAbleToCalculateScoreForScheduleGivenFullConstraint() throws Exception {
        Map<String, Plantao.Shift> days = new HashMap<>();
        days.put("MON", new Plantao.Shift(3, 4, 5));

        String shiftPlaceName = "n-1";
        Plantao plantao = Plantao.builder().name(shiftPlaceName).daysV3(days).build();
        Schedule schedule = new Schedule().convertShiftPlaceToSchedule(newArrayList(plantao));

        Map<ScoreFunction, List<String>> constraints = new HashMap<>(1);
        constraints.put(ScoreFunction.SHIFT, newArrayList("MORNING"));
        constraints.put(ScoreFunction.SHIFT_PLACE, newArrayList("n-1"));
        constraints.put(ScoreFunction.DAY, newArrayList("MON"));
        String brokerName = "John due";
        Schedule.BrokerV3 brokerV3 = new Schedule.BrokerV3(brokerName, "1", null, constraints);
        String harryPotter = "Harry Potter";
        Schedule.BrokerV3 harry = new Schedule.BrokerV3(harryPotter, "12", null, constraints);
        schedule.setBrokerV3s(newArrayList(brokerV3, harry));
        AlreadyScheduled alreadyScheduled = new AlreadyScheduled();
        Schedule scheduleFilled = builder.createSchedule(schedule, alreadyScheduled, new RandomNumber());

        Schedule scheduledWithScore = calculateScore.calculate(scheduleFilled);

        assertEquals(12, scheduledWithScore.getScore().intValue());
    }
}