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
    public void shouldBeAbleToReturn2GivenBrokerDoNotWantToWorkOnShiftPlace() {
        Map<String, Integer> shiftPlaceV3Map = new HashMap();
        shiftPlaceV3Map.put("n-01", 1);
        BrokerScore brokerScore = BrokerScore.builder()
                .shiftPlaces(shiftPlaceV3Map)
                .build();

        Integer score = ScoreFunction.SHIFT_PLACE.getConstraint().apply("n-01", brokerScore);

        assertEquals(2, score.intValue());
    }

    @Test
    public void shouldBeAbleToReturn0GivenBrokerDoesNotHaveConstraintShiftPlace() {
        Map<String, Integer> shiftPlaceV3Map = new HashMap();
        shiftPlaceV3Map.put("n-01", 1);
        BrokerScore brokerScore = BrokerScore.builder()
                .shiftPlaces(shiftPlaceV3Map)
                .build();

        Integer score = ScoreFunction.SHIFT_PLACE.getConstraint().apply("n-02", brokerScore);

        assertEquals(0, score.intValue());
    }

    @Test
    public void shouldBeAbleToReturn2GivenBrokerDoNotWantToWorkOnDay() {
        Map<DayEnum, Integer> day = new HashMap<>();
        day.put(MON, 1);
        BrokerScore brokerScore = BrokerScore.builder()
                .days(day)
                .build();

        Integer score = ScoreFunction.DAY.getConstraint().apply(MON.name(), brokerScore);

        assertEquals(2, score.intValue());
    }

    @Test
    public void shouldBeAbleToReturn0GivenBrokerDoesNotHaveConstraintDay() {
        Map<DayEnum, Integer> day = new HashMap<>();
        day.put(MON, 1);
        BrokerScore brokerScore = BrokerScore.builder()
                .days(day)
                .build();

        Integer score = ScoreFunction.DAY.getConstraint().apply(TUE.name(), brokerScore);

        assertEquals(0, score.intValue());
    }

    @Test
    public void shouldBeAbleToReturn2GivenBrokerDoNotWantToWorkOnShift() {
        Map<ShiftTimeEnum, Integer> shift = new HashMap();
        shift.put(MORNING, 1);
        BrokerScore brokerScore = BrokerScore.builder()
                .shifts(shift)
                .build();

        Integer score = ScoreFunction.SHIFT.getConstraint().apply(MORNING.name(), brokerScore);

        assertEquals(2, score.intValue());
    }

    @Test
    public void shouldBeAbleToReturn0GivenBrokerDoesNotHaveConstraintShift() {
        Map<ShiftTimeEnum, Integer> shift = new HashMap();
        shift.put(MORNING, 1);
        BrokerScore brokerScore = BrokerScore.builder()
                .shifts(shift)
                .build();

        Integer score = ScoreFunction.SHIFT.getConstraint().apply(AFTERNOON.name(), brokerScore);


        assertEquals(0, score.intValue());
    }

    @Test
    public void shouldBeAbleToReturn2GivenBrokerDoesNotPreferToWorkOnShiftPlace() {
        Map<String, Integer> shiftPlaceV3Map = new HashMap();
        shiftPlaceV3Map.put("n-01",1);
        BrokerScore brokerScore = BrokerScore.builder()
                .shiftPlaces(shiftPlaceV3Map)
                .build();

        Integer score = ScoreFunction.PARTIAL_SHIFT_PLACE.getConstraint().apply("n-01", brokerScore);

        assertEquals(1, score.intValue());
    }

    @Test
    public void shouldBeAbleToReturn0GivenBrokerDoesNotHaveConstraintToNotPreferShiftPlace() {
        Map<String, Integer> shiftPlaceV3Map = new HashMap();
        shiftPlaceV3Map.put("n-01",1);
        BrokerScore brokerScore = BrokerScore.builder()
                .shiftPlaces(shiftPlaceV3Map)
                .build();

        Integer score = ScoreFunction.PARTIAL_SHIFT_PLACE.getConstraint().apply("n-02", brokerScore);

        assertEquals(0, score.intValue());
    }

    @Test
    public void shouldBeAbleToReturn1GivenBrokerDoesNotPreferToWorkOnDay() {
        Map<DayEnum, Integer> day = new HashMap<>();
        day.put(MON, 1);
        BrokerScore brokerScore = BrokerScore.builder()
                .days(day)
                .build();

        Integer score = ScoreFunction.PARTIAL_DAY.getConstraint().apply(MON.name(), brokerScore);

        assertEquals(1, score.intValue());
    }

    @Test
    public void shouldBeAbleToReturn0GivenBrokerDoesNotHaveConstraintToNotPreferDay() {
        Map<DayEnum, Integer> day = new HashMap<>();
        day.put(MON, 1);
        BrokerScore brokerScore = BrokerScore.builder()
                .days(day)
                .build();

        Integer score = ScoreFunction.PARTIAL_DAY.getConstraint().apply(TUE.name(), brokerScore);

        assertEquals(0, score.intValue());
    }

    @Test
    public void shouldBeAbleToReturn1GivenBrokerDoNotPreferToWorkOnShift() {
        Map<ShiftTimeEnum, Integer> shift = new HashMap();
        shift.put(MORNING, 1);
        BrokerScore brokerScore = BrokerScore.builder()
                .shifts(shift)
                .build();

        Integer score = ScoreFunction.PARTIAL_SHIFT.getConstraint().apply(MORNING.name(), brokerScore);

        assertEquals(1, score.intValue());
    }

    @Test
    public void shouldBeAbleToReturn0GivenBrokerDoesNotHaveConstraintToPreferShift() {
        Map<ShiftTimeEnum, Integer> shift = new HashMap();
        shift.put(MORNING, 1);
        BrokerScore brokerScore = BrokerScore.builder()
                .shifts(shift)
                .build();

        Integer score = ScoreFunction.PARTIAL_SHIFT.getConstraint().apply(AFTERNOON.name(), brokerScore);

        assertEquals(0, score.intValue());
    }
}
