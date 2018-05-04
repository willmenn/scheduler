package com.broker.scheduler.service.v3.model;

import com.broker.scheduler.service.v2.model.Plantao;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.broker.scheduler.service.v3.model.DayEnum.MON;
import static com.broker.scheduler.service.v3.model.DayEnum.TUE;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

/**
 * Created by wahrons on 29/04/18.
 */
public class ScheduleTest {

    @Test
    public void shouldBeAbleToConvertShiftPlaceToScheduleGivenOnePlantao() {
        Map<DayEnum, Plantao.Shift> days = new HashMap<>();
        days.put(MON, new Plantao.Shift(3, 4, 5));
        days.put(TUE, new Plantao.Shift(3, 3, 6));

        Plantao plantao = Plantao.builder().daysV3(days).build();

        Schedule schedule = new Schedule().convertShiftPlaceToSchedule(newArrayList(plantao));

        Map<DayEnum, Schedule.Day> daysResponse = schedule.getShiftPlaceV3List().get(0).getDays();

        assertEquals(daysResponse.get(MON).getMorning().getMax(), 3);
        assertEquals(daysResponse.get(MON).getAfternoon().getMax(), 4);
        assertEquals(daysResponse.get(MON).getNight().getMax(), 5);

        assertEquals(daysResponse.get(TUE).getMorning().getMax(), 3);
        assertEquals(daysResponse.get(TUE).getAfternoon().getMax(), 3);
        assertEquals(daysResponse.get(TUE).getNight().getMax(), 6);
    }

    @Test
    public void shouldBeAbleToConvertShiftPlaceToScheduleGivenTwoPlantao() {
        Map<DayEnum, Plantao.Shift> days = new HashMap<>();
        days.put(MON, new Plantao.Shift(3, 4, 5));

        Plantao plantao = Plantao.builder().name("n-1").daysV3(days).build();

        Map<DayEnum, Plantao.Shift> days1 = new HashMap<>();
        days1.put(MON, new Plantao.Shift(1, 2, 3));

        Plantao plantao1 = Plantao.builder().name("n-2").daysV3(days1).build();
        Schedule schedule = new Schedule().convertShiftPlaceToSchedule(newArrayList(plantao, plantao1));

        Map<DayEnum, Schedule.Day> daysResponse = schedule.getShiftPlaceV3List().get(0).getDays();
        Map<DayEnum, Schedule.Day> daysResponse1 = schedule.getShiftPlaceV3List().get(1).getDays();

        assertEquals(daysResponse.get(MON).getMorning().getMax(), 3);
        assertEquals(daysResponse.get(MON).getAfternoon().getMax(), 4);
        assertEquals(daysResponse.get(MON).getNight().getMax(), 5);

        assertEquals(daysResponse1.get(MON).getMorning().getMax(), 1);
        assertEquals(daysResponse1.get(MON).getAfternoon().getMax(), 2);
        assertEquals(daysResponse1.get(MON).getNight().getMax(), 3);
    }


    @Test
    public void shouldBeAbleToRemoveBrokerGivenScoreBiggerThanThreshold() {
        Schedule.Shift shift = new Schedule.Shift(ShiftTimeEnum.MORNING,
                newArrayList(new Schedule.BrokerV3("John Due","id",BigDecimal.valueOf(2),null)),1);

        shift.removeBroker(1.);

        assertEquals(0, shift.getBrokerV3List().size());
    }

    @Test
    public void shouldBeAbleToRemoveBrokerGivenScoreBiggerThanThresholdPutKeepOneBroker() {
        Schedule.Shift shift = new Schedule.Shift(ShiftTimeEnum.MORNING,
                newArrayList(new Schedule.BrokerV3("John Due","id",BigDecimal.valueOf(3),null),
                        new Schedule.BrokerV3("Harry Potter","id",BigDecimal.valueOf(1),null)),1);

        List<Schedule.BrokerV3> brokerV3s = shift.removeBroker(2.);

        assertEquals(1, shift.getBrokerV3List().size());
        assertEquals("Harry Potter", shift.getBrokerV3List().get(0).getName());
        assertEquals("John Due", brokerV3s.get(0).getName());
        assertEquals(1, brokerV3s.size());
    }

    @Test
    public void shouldBeAbleToRemoveBrokerFromScheduleGivenScoreBiggerThanThresholdPutKeepOneBroker() {
        Schedule.Shift shift = new Schedule.Shift(ShiftTimeEnum.MORNING,
                newArrayList(new Schedule.BrokerV3("John Due","id",BigDecimal.valueOf(3),null),
                        new Schedule.BrokerV3("Harry Potter","id",BigDecimal.valueOf(1),null)),1);

        Schedule.ShiftPlaceV3 sp = new Schedule.ShiftPlaceV3("plantao 1", "id");
        sp.getDays().get(MON).setMorning(shift);
        Schedule schedule = new Schedule();
        schedule.getShiftPlaceV3List().add(sp);
        List<Schedule.BrokerV3> brokerV3s = schedule.removeAllBrokersForThreshold(2.);

        assertEquals(1, shift.getBrokerV3List().size());
        assertEquals("Harry Potter", shift.getBrokerV3List().get(0).getName());
        assertEquals("John Due", brokerV3s.get(0).getName());
        assertEquals(1, brokerV3s.size());
    }
}