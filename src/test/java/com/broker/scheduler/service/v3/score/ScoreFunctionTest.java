package com.broker.scheduler.service.v3.score;

import com.broker.scheduler.service.v3.model.DayEnum;
import com.broker.scheduler.service.v3.model.Schedule;
import com.broker.scheduler.service.v3.model.ShiftTimeEnum;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.broker.scheduler.service.v3.model.DayEnum.MON;
import static com.broker.scheduler.service.v3.model.DayEnum.TUE;
import static com.broker.scheduler.service.v3.model.ShiftTimeEnum.AFTERNOON;
import static com.broker.scheduler.service.v3.model.ShiftTimeEnum.MORNING;
import static org.junit.Assert.assertEquals;

/**
 * Created by wahrons on 30/04/18.
 */
public class ScoreFunctionTest {

    @Test
    public void shouldBeAbleToReturn2GivenBrokerDoNotWantToWorkOnShiftPlace() throws Exception {
        Map<String, Schedule.ShiftPlaceV3> shiftPlaceV3Map = new HashMap();
        shiftPlaceV3Map.put("n-01", null);
        BrokerScore brokerScore = BrokerScore.builder()
                .shiftPlaces(shiftPlaceV3Map)
                .build();

        Integer score = ScoreFunction.SHIFT_PLACE.getConstraint().apply("n-01", brokerScore);

        assertEquals(2, score.intValue());
    }

    @Test
    public void shouldBeAbleToReturn0GivenBrokerDoesNotHaveConstraintShiftPlace() throws Exception {
        Map<String, Schedule.ShiftPlaceV3> shiftPlaceV3Map = new HashMap();
        shiftPlaceV3Map.put("n-01", null);
        BrokerScore brokerScore = BrokerScore.builder()
                .shiftPlaces(shiftPlaceV3Map)
                .build();

        Integer score = ScoreFunction.SHIFT_PLACE.getConstraint().apply("n-02", brokerScore);

        assertEquals(0, score.intValue());
    }

    @Test
    public void shouldBeAbleToReturn2GivenBrokerDoNotWantToWorkOnDay() throws Exception {
        Map<DayEnum, Schedule.Day> day = new HashMap<>();
        day.put(MON, null);
        BrokerScore brokerScore = BrokerScore.builder()
                .days(day)
                .build();

        Integer score = ScoreFunction.DAY.getConstraint().apply(MON.name(), brokerScore);

        assertEquals(2, score.intValue());
    }

    @Test
    public void shouldBeAbleToReturn0GivenBrokerDoesNotHaveConstraintDay() throws Exception {
        Map<DayEnum, Schedule.Day> day = new HashMap<>();
        day.put(MON, null);
        BrokerScore brokerScore = BrokerScore.builder()
                .days(day)
                .build();

        Integer score = ScoreFunction.DAY.getConstraint().apply(TUE.name(), brokerScore);

        assertEquals(0, score.intValue());
    }

    @Test
    public void shouldBeAbleToReturn2GivenBrokerDoNotWantToWorkOnShift() throws Exception {
        Map<ShiftTimeEnum, Schedule.Shift> shift = new HashMap();
        shift.put(MORNING, null);
        BrokerScore brokerScore = BrokerScore.builder()
                .shifts(shift)
                .build();

        Integer score = ScoreFunction.SHIFT.getConstraint().apply(MORNING.name(), brokerScore);

        assertEquals(2, score.intValue());
    }

    @Test
    public void shouldBeAbleToReturn0GivenBrokerDoesNotHaveConstraintShift() throws Exception {
        Map<ShiftTimeEnum, Schedule.Shift> shift = new HashMap();
        shift.put(MORNING, null);
        BrokerScore brokerScore = BrokerScore.builder()
                .shifts(shift)
                .build();

        Integer score = ScoreFunction.SHIFT.getConstraint().apply(AFTERNOON.name(), brokerScore);


        assertEquals(0, score.intValue());
    }

    @Test
    public void shouldBeAbleToReturn2GivenBrokerDoesNotPreferToWorkOnShiftPlace() throws Exception {
        Map<String, Schedule.ShiftPlaceV3> shiftPlaceV3Map = new HashMap();
        shiftPlaceV3Map.put("n-01", null);
        BrokerScore brokerScore = BrokerScore.builder()
                .shiftPlaces(shiftPlaceV3Map)
                .build();

        Integer score = ScoreFunction.PARTIAL_SHIFT_PLACE.getConstraint().apply("n-01", brokerScore);

        assertEquals(1, score.intValue());
    }

    @Test
    public void shouldBeAbleToReturn0GivenBrokerDoesNotHaveConstraintToNotPreferShiftPlace() throws Exception {
        Map<String, Schedule.ShiftPlaceV3> shiftPlaceV3Map = new HashMap();
        shiftPlaceV3Map.put("n-01", null);
        BrokerScore brokerScore = BrokerScore.builder()
                .shiftPlaces(shiftPlaceV3Map)
                .build();

        Integer score = ScoreFunction.PARTIAL_SHIFT_PLACE.getConstraint().apply("n-02", brokerScore);

        assertEquals(0, score.intValue());
    }

    @Test
    public void shouldBeAbleToReturn1GivenBrokerDoesNotPreferToWorkOnDay() throws Exception {
        Map<DayEnum, Schedule.Day> day = new HashMap<>();
        day.put(MON, null);
        BrokerScore brokerScore = BrokerScore.builder()
                .days(day)
                .build();

        Integer score = ScoreFunction.PARTIAL_DAY.getConstraint().apply(MON.name(), brokerScore);

        assertEquals(1, score.intValue());
    }

    @Test
    public void shouldBeAbleToReturn0GivenBrokerDoesNotHaveConstraintToNotPreferDay() throws Exception {
        Map<DayEnum, Schedule.Day> day = new HashMap<>();
        day.put(MON, null);
        BrokerScore brokerScore = BrokerScore.builder()
                .days(day)
                .build();

        Integer score = ScoreFunction.PARTIAL_DAY.getConstraint().apply(TUE.name(), brokerScore);

        assertEquals(0, score.intValue());
    }

    @Test
    public void shouldBeAbleToReturn1GivenBrokerDoNotPreferToWorkOnShift() throws Exception {
        Map<ShiftTimeEnum, Schedule.Shift> shift = new HashMap();
        shift.put(MORNING, null);
        BrokerScore brokerScore = BrokerScore.builder()
                .shifts(shift)
                .build();

        Integer score = ScoreFunction.PARTIAL_SHIFT.getConstraint().apply(MORNING.name(), brokerScore);

        assertEquals(1, score.intValue());
    }

    @Test
    public void shouldBeAbleToReturn0GivenBrokerDoesNotHaveConstraintToPreferShift() throws Exception {
        Map<ShiftTimeEnum, Schedule.Shift> shift = new HashMap();
        shift.put(MORNING, null);
        BrokerScore brokerScore = BrokerScore.builder()
                .shifts(shift)
                .build();

        Integer score = ScoreFunction.PARTIAL_SHIFT.getConstraint().apply(AFTERNOON.name(), brokerScore);

        assertEquals(0, score.intValue());
    }
}