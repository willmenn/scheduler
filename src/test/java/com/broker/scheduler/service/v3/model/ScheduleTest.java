package com.broker.scheduler.service.v3.model;

import com.broker.scheduler.service.v2.model.Plantao;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

/**
 * Created by wahrons on 29/04/18.
 */
public class ScheduleTest {

    @Test
    public void shouldBeAbleToConvertShiftPlaceToScheduleGivenOnePlantao() throws Exception {
        Map<String, Plantao.Shift> days = new HashMap<>();
        days.put("MON", new Plantao.Shift(3, 4, 5));
        days.put("TUE", new Plantao.Shift(3, 3, 6));

        Plantao plantao = Plantao.builder().daysV3(days).build();

        Schedule schedule = new Schedule().convertShiftPlaceToSchedule(newArrayList(plantao));

        Map<String, Schedule.Day> daysResponse = schedule.getShiftPlaceV3List().get(0).getDays();

        assertEquals(daysResponse.get("MON").getMorning().getMax().intValue(), 3);
        assertEquals(daysResponse.get("MON").getAfternoon().getMax().intValue(), 4);
        assertEquals(daysResponse.get("MON").getNight().getMax().intValue(), 5);

        assertEquals(daysResponse.get("TUE").getMorning().getMax().intValue(), 3);
        assertEquals(daysResponse.get("TUE").getAfternoon().getMax().intValue(), 3);
        assertEquals(daysResponse.get("TUE").getNight().getMax().intValue(), 6);
    }

    @Test
    public void shouldBeAbleToConvertShiftPlaceToScheduleGivenTwoPlantao() throws Exception {
        Map<String, Plantao.Shift> days = new HashMap<>();
        days.put("MON", new Plantao.Shift(3, 4, 5));

        Plantao plantao = Plantao.builder().name("n-1").daysV3(days).build();

        Map<String, Plantao.Shift> days1 = new HashMap<>();
        days1.put("MON", new Plantao.Shift(1, 2, 3));

        Plantao plantao1 = Plantao.builder().name("n-2").daysV3(days1).build();
        Schedule schedule = new Schedule().convertShiftPlaceToSchedule(newArrayList(plantao, plantao1));

        Map<String, Schedule.Day> daysResponse = schedule.getShiftPlaceV3List().get(0).getDays();
        Map<String, Schedule.Day> daysResponse1 = schedule.getShiftPlaceV3List().get(1).getDays();

        assertEquals(daysResponse.get("MON").getMorning().getMax().intValue(), 3);
        assertEquals(daysResponse.get("MON").getAfternoon().getMax().intValue(), 4);
        assertEquals(daysResponse.get("MON").getNight().getMax().intValue(), 5);

        assertEquals(daysResponse1.get("MON").getMorning().getMax().intValue(), 1);
        assertEquals(daysResponse1.get("MON").getAfternoon().getMax().intValue(), 2);
        assertEquals(daysResponse1.get("MON").getNight().getMax().intValue(), 3);
    }

}