package com.broker.scheduler.service.v3.score;

import com.broker.scheduler.service.v2.model.Plantao;
import com.broker.scheduler.service.v3.ScheduleBuilder;
import com.broker.scheduler.service.v3.model.AlreadyScheduled;
import com.broker.scheduler.service.v3.model.DayEnum;
import com.broker.scheduler.service.v3.model.FakeRandomNumber;
import com.broker.scheduler.service.v3.model.RandomNumber;
import com.broker.scheduler.service.v3.model.Schedule;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.broker.scheduler.service.v3.helper.ScheduleHelper.buildOnePlantaoMon345WithOneBroker;
import static com.broker.scheduler.service.v3.model.DayEnum.MON;
import static com.broker.scheduler.service.v3.model.DayEnum.TUE;
import static com.broker.scheduler.service.v3.model.ShiftTimeEnum.AFTERNOON;
import static com.broker.scheduler.service.v3.model.ShiftTimeEnum.MORNING;
import static com.broker.scheduler.service.v3.model.ShiftTimeEnum.NIGHT;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by wahrons on 30/04/18.
 */
public class BrokerScoreTest {

    private ScheduleBuilder builder;

    @Before
    public void setUp() {
        builder = new ScheduleBuilder();
    }

    @Test
    public void shouldBeAbleToCreateBrokerScoreMapGivenSchedule() {
        Schedule schedule = buildOnePlantaoMon345WithOneBroker(null);
        Map<String, BrokerScore> brokerScoreMap = new BrokerScore().buildBrokerScoreMap(schedule);

        String brokerName = "John due";
        String shiftPlaceName = "n-1";
        assertTrue(brokerScoreMap.containsKey(brokerName));
        BrokerScore brokerScore = brokerScoreMap.get(brokerName);
        assertTrue(brokerScore.getDays().containsKey(MON));
        assertTrue(brokerScore.getShifts().containsKey(MORNING));
        assertTrue(brokerScore.getShifts().containsKey(AFTERNOON));
        assertTrue(brokerScore.getShifts().containsKey(NIGHT));
        assertTrue(brokerScore.getShiftPlaces().containsKey(shiftPlaceName));
    }

    @Test
    public void shouldBeAbleToCreateBrokerScoreMapGivenScheduleWithTwoBrokers() {
        Map<DayEnum, Plantao.Shift> days = new HashMap<>();
        days.put(MON, new Plantao.Shift(1, 2, 1));

        String shiftPlaceName = "n-1";
        Plantao plantao = Plantao.builder().name(shiftPlaceName).daysV3(days).build();
        Schedule schedule = new Schedule().convertShiftPlaceToSchedule(newArrayList(plantao));

        String johnDue = "John due";
        Schedule.BrokerV3 brokerV3 = new Schedule.BrokerV3(johnDue, "1", null, null);
        String harryPotter = "Harry Potter";
        Schedule.BrokerV3 brokerV31 = new Schedule.BrokerV3(harryPotter, "2", null, null);
        schedule.setBrokerV3s(newArrayList(brokerV3, brokerV31));

        AlreadyScheduled alreadyScheduled = new AlreadyScheduled();

        Schedule scheduleFilled = builder.createSchedule(schedule, alreadyScheduled, new FakeRandomNumber());

        Map<String, BrokerScore> brokerScoreMap = new BrokerScore().buildBrokerScoreMap(scheduleFilled);

        assertTrue(brokerScoreMap.containsKey(johnDue));
        BrokerScore brokerJohn = brokerScoreMap.get(johnDue);
        assertTrue(brokerJohn.getDays().containsKey(MON));
        assertTrue(brokerJohn.getShifts().containsKey(MORNING));
        assertTrue(brokerJohn.getShifts().containsKey(AFTERNOON));
        assertTrue(brokerJohn.getShifts().containsKey(NIGHT));
        assertTrue(brokerJohn.getShiftPlaces().containsKey(shiftPlaceName));

        BrokerScore brokerHarryPotter = brokerScoreMap.get(harryPotter);

        assertTrue(brokerHarryPotter.getShifts().containsKey(AFTERNOON));

    }

    @Test
    public void shouldBeAbleToCreateBrokerScoreMapGivenScheduleWithTwoBrokersAndTwoDays() {
        Map<DayEnum, Plantao.Shift> days = new HashMap<>();
        days.put(MON, new Plantao.Shift(0, 1, 1));
        days.put(TUE, new Plantao.Shift(0, 1, 2));

        String shiftPlaceName = "n-1";
        Plantao plantao = Plantao.builder().name(shiftPlaceName).daysV3(days).build();
        Schedule schedule = new Schedule().convertShiftPlaceToSchedule(newArrayList(plantao));

        String johnDue = "John due";
        Schedule.BrokerV3 brokerV3 = new Schedule.BrokerV3(johnDue, "1", null, null);
        String harryPotter = "Harry Potter";
        Schedule.BrokerV3 brokerV31 = new Schedule.BrokerV3(harryPotter, "2", null, null);
        schedule.setBrokerV3s(newArrayList(brokerV3, brokerV31));

        AlreadyScheduled alreadyScheduled = new AlreadyScheduled();

        Schedule scheduleFilled = builder.createSchedule(schedule, alreadyScheduled, new FakeRandomNumber());

        Map<String, BrokerScore> brokerScoreMap = new BrokerScore().buildBrokerScoreMap(scheduleFilled);

        assertTrue(brokerScoreMap.containsKey(johnDue));
        BrokerScore brokerJohn = brokerScoreMap.get(johnDue);
        assertTrue(brokerJohn.getDays().containsKey(MON));
        assertTrue(brokerJohn.getDays().containsKey(TUE));
        assertFalse(brokerJohn.getShifts().containsKey(MORNING));
        assertTrue(brokerJohn.getShifts().containsKey(AFTERNOON));
        assertTrue(brokerJohn.getShifts().containsKey(NIGHT));
        assertTrue(brokerJohn.getShiftPlaces().containsKey(shiftPlaceName));

        BrokerScore brokerHarryPotter = brokerScoreMap.get(harryPotter);

        assertTrue(brokerHarryPotter.getDays().containsKey(TUE));
        assertFalse(brokerHarryPotter.getShifts().containsKey(AFTERNOON));
        assertTrue(brokerHarryPotter.getShifts().containsKey(NIGHT));
    }

    @Test
    public void shouldBeAbleToCalculateScore() {
        Map<ScoreFunction, List<String>> constraints = new HashMap<>(1);
        constraints.put(ScoreFunction.SHIFT_PLACE, newArrayList("n-1"));

        Schedule schedule = buildOnePlantaoMon345WithOneBroker(constraints);

        Map<String, BrokerScore> brokerScoreMap = new BrokerScore().buildBrokerScoreMap(schedule);
        int score = brokerScoreMap.get("John due").calculateScore();

        assertEquals(2, score);
        assertEquals(2, brokerScoreMap.get("John due").getScore());
        assertEquals(2, brokerScoreMap.get("John due").getBrokerV3().getScore().intValue());
    }

    @Test
    public void shouldBeAbleToCalculateScoreForDay() {
        Map<ScoreFunction, List<String>> constraints = new HashMap<>(1);
        constraints.put(ScoreFunction.DAY, newArrayList("MON"));

        Schedule schedule = buildOnePlantaoMon345WithOneBroker(constraints);

        Map<String, BrokerScore> brokerScoreMap = new BrokerScore().buildBrokerScoreMap(schedule);
        int score = brokerScoreMap.get("John due").calculateScore();

        assertEquals(2, score);
        assertEquals(2, brokerScoreMap.get("John due").getScore());
    }

    @Test
    public void shouldBeAbleToCalculateScoreForShift() {
        Map<ScoreFunction, List<String>> constraints = new HashMap<>(1);
        constraints.put(ScoreFunction.SHIFT, newArrayList("MORNING"));
        Schedule schedule = buildOnePlantaoMon345WithOneBroker(constraints);

        Map<String, BrokerScore> brokerScoreMap = new BrokerScore().buildBrokerScoreMap(schedule);
        int score = brokerScoreMap.get("John due").calculateScore();

        assertEquals(2, score);
        assertEquals(2, brokerScoreMap.get("John due").getScore());
    }


    @Test
    public void shouldBeAbleToCalculateScoreForPartials() {
        Map<ScoreFunction, List<String>> constraints = new HashMap<>(1);
        constraints.put(ScoreFunction.PARTIAL_SHIFT, newArrayList("MORNING"));
        constraints.put(ScoreFunction.PARTIAL_SHIFT_PLACE, newArrayList("n-1"));
        constraints.put(ScoreFunction.PARTIAL_DAY, newArrayList("MON"));

        Schedule schedule = buildOnePlantaoMon345WithOneBroker(constraints);

        Map<String, BrokerScore> brokerScoreMap = new BrokerScore().buildBrokerScoreMap(schedule);
        int score = brokerScoreMap.get("John due").calculateScore();

        assertEquals(3, score);
        assertEquals(3, brokerScoreMap.get("John due").getScore());
    }
}