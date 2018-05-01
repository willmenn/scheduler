package com.broker.scheduler.service.v3;

import com.broker.scheduler.service.v2.model.Plantao;
import com.broker.scheduler.service.v3.model.AlreadyScheduled;
import com.broker.scheduler.service.v3.model.FakeRandomNumber;
import com.broker.scheduler.service.v3.model.Schedule;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.broker.scheduler.service.v3.model.DayEnum.MON;
import static com.broker.scheduler.service.v3.model.DayEnum.TUE;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

/**
 * Created by wahrons on 29/04/18.
 */
public class ScheduleBuilderTest {

    private ScheduleBuilder builder;

    @Before
    public void setUp() throws Exception {
        builder = new ScheduleBuilder();
    }

    @Test
    public void shouldBeAbleToScheduleOneBrokerGivenZeroOnAlreadySchedule() throws Exception {
        Map<String, Plantao.Shift> days = new HashMap<>();
        days.put("MON", new Plantao.Shift(3, 4, 5));

        Plantao plantao = Plantao.builder().name("n-1").daysV3(days).build();
        Schedule schedule = new Schedule().convertShiftPlaceToSchedule(newArrayList(plantao));

        Schedule.BrokerV3 brokerV3 = new Schedule.BrokerV3("John due", "1",null,null);
        schedule.setBrokerV3s(newArrayList(brokerV3));

        AlreadyScheduled alreadyScheduled = new AlreadyScheduled();

        Schedule response = builder.createSchedule(schedule, alreadyScheduled, new FakeRandomNumber());

        Schedule.Day day = response.getShiftPlaceV3List().get(0).getDays().get(MON);
        assertEquals(1, day.getMorning().getBrokerV3List().size());
        assertEquals(1, day.getAfternoon().getBrokerV3List().size());
        assertEquals(1, day.getNight().getBrokerV3List().size());
    }

    @Test
    public void shouldBeAbleToScheduleTwoBrokerGivenZeroOnAlreadySchedule() throws Exception {
        Map<String, Plantao.Shift> days = new HashMap<>();
        days.put("MON", new Plantao.Shift(1, 2, 1));

        Plantao plantao = Plantao.builder().name("n-1").daysV3(days).build();
        Schedule schedule = new Schedule().convertShiftPlaceToSchedule(newArrayList(plantao));

        Schedule.BrokerV3 brokerV3 = new Schedule.BrokerV3("John due", "1",null, null);
        Schedule.BrokerV3 brokerV31 = new Schedule.BrokerV3("Harry Potter", "2", null, null);
        schedule.setBrokerV3s(newArrayList(brokerV3, brokerV31));

        AlreadyScheduled alreadyScheduled = new AlreadyScheduled();

        Schedule response = builder.createSchedule(schedule, alreadyScheduled, new FakeRandomNumber());

        Schedule.Day day = response.getShiftPlaceV3List().get(0).getDays().get(MON);
        assertEquals(1, day.getMorning().getBrokerV3List().size());
        assertEquals(2, day.getAfternoon().getBrokerV3List().size());
        assertEquals(1, day.getNight().getBrokerV3List().size());
    }

    @Test
    public void shouldBeAbleToScheduleTwoBrokerGivenZeroOnAlreadyScheduleAndTwoDays() throws Exception {
        Map<String, Plantao.Shift> days = new HashMap<>();
        days.put("MON", new Plantao.Shift(1, 2, 1));
        days.put("TUE", new Plantao.Shift(2, 1, 1));

        Plantao plantao = Plantao.builder().name("n-1").daysV3(days).build();
        Schedule schedule = new Schedule().convertShiftPlaceToSchedule(newArrayList(plantao));

        Schedule.BrokerV3 brokerV3 = new Schedule.BrokerV3("John due", "1", null, null);
        Schedule.BrokerV3 brokerV31 = new Schedule.BrokerV3("Harry Potter", "2", null, null);
        schedule.setBrokerV3s(newArrayList(brokerV3, brokerV31));

        AlreadyScheduled alreadyScheduled = new AlreadyScheduled();

        Schedule response = builder.createSchedule(schedule, alreadyScheduled, new FakeRandomNumber());

        Schedule.Day mon = response.getShiftPlaceV3List().get(0).getDays().get(MON);
        assertEquals(1, mon.getMorning().getBrokerV3List().size());
        assertEquals(2, mon.getAfternoon().getBrokerV3List().size());
        assertEquals(1, mon.getNight().getBrokerV3List().size());

        Schedule.Day tue = response.getShiftPlaceV3List().get(0).getDays().get(TUE);
        assertEquals(2, tue.getMorning().getBrokerV3List().size());
        assertEquals(1, tue.getAfternoon().getBrokerV3List().size());
        assertEquals(1, tue.getNight().getBrokerV3List().size());
    }

    @Test
    public void shouldBeAbleToScheduleTwoBrokerGivenZeroOnAlreadyScheduleAndTwoShiftPlaces() throws Exception {
        Map<String, Plantao.Shift> days = new HashMap<>();
        days.put("MON", new Plantao.Shift(1, 2, 1));

        Plantao plantao = Plantao.builder().name("n-1").daysV3(days).build();

        Map<String, Plantao.Shift> days2 = new HashMap<>();
        days2.put("MON", new Plantao.Shift(1, 2, 1));

        Plantao plantao2 = Plantao.builder().name("n-2").daysV3(days2).build();
        Schedule schedule = new Schedule().convertShiftPlaceToSchedule(newArrayList(plantao, plantao2));

        Schedule.BrokerV3 brokerV3 = new Schedule.BrokerV3("John due", "1", null, null);
        Schedule.BrokerV3 brokerV31 = new Schedule.BrokerV3("Harry Potter", "2", null, null);
        schedule.setBrokerV3s(newArrayList(brokerV3, brokerV31));

        AlreadyScheduled alreadyScheduled = new AlreadyScheduled();

        Schedule response = builder.createSchedule(schedule, alreadyScheduled, new FakeRandomNumber());

        Schedule.Day mon = response.getShiftPlaceV3List().get(0).getDays().get(MON);
        assertEquals(1, mon.getMorning().getBrokerV3List().size());
        assertEquals(2, mon.getAfternoon().getBrokerV3List().size());
        assertEquals(1, mon.getNight().getBrokerV3List().size());

        Schedule.Day tue = response.getShiftPlaceV3List().get(1).getDays().get(MON);
        assertEquals(1, tue.getMorning().getBrokerV3List().size());
        assertEquals(0, tue.getAfternoon().getBrokerV3List().size());
        assertEquals(1, tue.getNight().getBrokerV3List().size());
    }
}
