package com.broker.scheduler.service.v3.score;

import com.broker.scheduler.service.v2.model.Plantao;
import com.broker.scheduler.service.v3.ScheduleBuilder;
import com.broker.scheduler.service.v3.model.AlreadyScheduled;
import com.broker.scheduler.service.v3.model.FakeRandomNumber;
import com.broker.scheduler.service.v3.model.RandomNumber;
import com.broker.scheduler.service.v3.model.Schedule;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.broker.scheduler.service.v3.model.DayEnum.MON;
import static com.broker.scheduler.service.v3.model.DayEnum.TUE;
import static com.broker.scheduler.service.v3.model.ShiftTimeEnum.AFTERNOON;
import static com.broker.scheduler.service.v3.model.ShiftTimeEnum.MORNING;
import static com.broker.scheduler.service.v3.model.ShiftTimeEnum.NIGHT;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by wahrons on 30/04/18.
 */
public class BrokerScoreTest {

    private ScheduleBuilder builder;
    private ScheduleBuilder scheduleBuilder;

    @Before
    public void setUp() throws Exception {
        scheduleBuilder = builder = new ScheduleBuilder();
    }

    @Test
    public void shouldBeAbleToCreateBrokerScoreMapGivenSchedule() throws Exception {
        Map<String, Plantao.Shift> days = new HashMap<>();
        days.put("MON", new Plantao.Shift(3, 4, 5));

        String shiftPlaceName = "n-1";
        Plantao plantao = Plantao.builder().name(shiftPlaceName).daysV3(days).build();
        Schedule schedule = new Schedule().convertShiftPlaceToSchedule(newArrayList(plantao));

        String brokerName = "John due";
        Schedule.BrokerV3 brokerV3 = new Schedule.BrokerV3(brokerName, "1", null);
        schedule.setBrokerV3s(newArrayList(brokerV3));
        AlreadyScheduled alreadyScheduled = new AlreadyScheduled();
        Schedule schedulFilled = builder.createSchedule(schedule, alreadyScheduled, new RandomNumber());

        Map<String, BrokerScore> brokerScoreMap = new BrokerScore().buildBrokerScoreMap(schedulFilled);

        assertTrue(brokerScoreMap.containsKey(brokerName));
        BrokerScore brokerScore = brokerScoreMap.get(brokerName);
        assertTrue(brokerScore.getDays().containsKey(MON));
        assertTrue(brokerScore.getShifts().containsKey(MORNING));
        assertTrue(brokerScore.getShifts().containsKey(AFTERNOON));
        assertTrue(brokerScore.getShifts().containsKey(NIGHT));
        assertTrue(brokerScore.getShiftPlaces().containsKey(shiftPlaceName));
    }

    @Test
    public void shouldBeAbleToCreateBrokerScoreMapGivenScheduleWithTwoBrokers() throws Exception {
        Map<String, Plantao.Shift> days = new HashMap<>();
        days.put("MON", new Plantao.Shift(1, 2, 1));

        String shiftPlaceName = "n-1";
        Plantao plantao = Plantao.builder().name(shiftPlaceName).daysV3(days).build();
        Schedule schedule = new Schedule().convertShiftPlaceToSchedule(newArrayList(plantao));

        String johnDue = "John due";
        Schedule.BrokerV3 brokerV3 = new Schedule.BrokerV3(johnDue, "1", null);
        String harryPotter = "Harry Potter";
        Schedule.BrokerV3 brokerV31 = new Schedule.BrokerV3(harryPotter, "2", null);
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
    public void shouldBeAbleToCreateBrokerScoreMapGivenScheduleWithTwoBrokersAndTwoDays() throws Exception {
        Map<String, Plantao.Shift> days = new HashMap<>();
        days.put("MON", new Plantao.Shift(0, 1, 1));
        days.put("TUE", new Plantao.Shift(0, 1, 2));

        String shiftPlaceName = "n-1";
        Plantao plantao = Plantao.builder().name(shiftPlaceName).daysV3(days).build();
        Schedule schedule = new Schedule().convertShiftPlaceToSchedule(newArrayList(plantao));

        String johnDue = "John due";
        Schedule.BrokerV3 brokerV3 = new Schedule.BrokerV3(johnDue, "1", null);
        String harryPotter = "Harry Potter";
        Schedule.BrokerV3 brokerV31 = new Schedule.BrokerV3(harryPotter, "2", null);
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
}