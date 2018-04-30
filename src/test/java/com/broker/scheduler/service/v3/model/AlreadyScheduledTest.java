package com.broker.scheduler.service.v3.model;

import org.junit.Test;

import static com.broker.scheduler.service.v3.model.DayEnum.MON;
import static com.broker.scheduler.service.v3.model.DayEnum.SAT;
import static com.broker.scheduler.service.v3.model.DayEnum.SUN;
import static com.broker.scheduler.service.v3.model.DayEnum.TUE;
import static com.broker.scheduler.service.v3.model.DayEnum.WED;
import static com.broker.scheduler.service.v3.model.ShiftTimeEnum.AFTERNOON;
import static com.broker.scheduler.service.v3.model.ShiftTimeEnum.MORNING;
import static com.broker.scheduler.service.v3.model.ShiftTimeEnum.NIGHT;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by wahrons on 29/04/18.
 */
public class AlreadyScheduledTest {

    @Test
    public void shouldReturnFalseGivenNoBrokerAlreadyScheduled() throws Exception {
        AlreadyScheduled scheduled = new AlreadyScheduled();
        assertFalse(scheduled.containsBrokerOnDayShift(SAT, MORNING, "John Due"));
    }

    @Test
    public void shouldReturnTrueGivenBrokerAlreadyScheduledMorning() throws Exception {
        AlreadyScheduled scheduled = new AlreadyScheduled();
        scheduled.addBroker(SUN, MORNING, Schedule.BrokerV3.builder().name("Harry Potter").build());
        assertFalse(scheduled.containsBrokerOnDayShift(MON, MORNING, "John Due"));
        assertTrue(scheduled.containsBrokerOnDayShift(SUN, MORNING, "Harry Potter"));
    }

    @Test
    public void shouldReturnTrueGivenBrokerAlreadyScheduledAfternoon() throws Exception {
        AlreadyScheduled scheduled = new AlreadyScheduled();
        scheduled.addBroker(WED, AFTERNOON, Schedule.BrokerV3.builder().name("Gandalf").build());
        assertFalse(scheduled.containsBrokerOnDayShift(MON, MORNING, "John Due"));
        assertTrue(scheduled.containsBrokerOnDayShift(WED, AFTERNOON, "Gandalf"));
    }

    @Test
    public void shouldReturnTrueGivenBrokerAlreadyScheduledNight() throws Exception {
        AlreadyScheduled scheduled = new AlreadyScheduled();
        scheduled.addBroker(TUE, NIGHT, Schedule.BrokerV3.builder().name("Frodo").build());
        assertFalse(scheduled.containsBrokerOnDayShift(MON, MORNING, "John Due"));
        assertTrue(scheduled.containsBrokerOnDayShift(TUE, NIGHT, "Frodo"));
    }


}