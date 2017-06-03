package com.broker.scheduler.service;


import com.broker.scheduler.model.Broker;
import com.broker.scheduler.model.DaySchedule;
import com.broker.scheduler.model.Preference;
import com.broker.scheduler.model.ShiftPlace;
import com.broker.scheduler.model.WeekSchedule;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BuildWeekScheduleTest {


    private BuildWeekSchedule buildWeekSchedule;

    @Before
    public void setUp() throws Exception {
        buildWeekSchedule = new BuildWeekSchedule(new FulfillTheWholesOfWeekSchedule());
    }

    @Test
    public void shouldBeAbleToCreateASchedule() throws Exception {
        Broker broker1 = Broker.builder()
                .brokerId("bmon")
                .name("bomn-name")
                .preference(Preference.builder().weekDay("MON").build())
                .build();
        Broker broker11 = Broker.builder()
                .brokerId("bmon1")
                .name("bomn-name")
                .preference(Preference.builder().weekDay("MON").build())
                .build();
        Broker broker2 = Broker.builder()
                .brokerId("btue")
                .name("btue-name")
                .preference(Preference.builder().weekDay("TUE").build())
                .build();
        List<Broker> brokers = Arrays.asList(broker1, broker2, broker11);
        ShiftPlace shiftPlaceMON = ShiftPlace.builder()
                .days(Arrays.asList("MON"))
                .places("2")
                .managersName("MN")
                .shiftPlaceId("SP-MON")
                .build();
        ShiftPlace shiftPlaceTUE = ShiftPlace.builder()
                .days(Arrays.asList("TUE"))
                .places("1")
                .managersName("MN1")
                .shiftPlaceId("SP-TUE")
                .build();
        List<ShiftPlace> shiftPlaces = Arrays.asList(shiftPlaceMON, shiftPlaceTUE);

        WeekSchedule weekSchedule = buildWeekSchedule.buildDaySchedule(brokers, shiftPlaces);
        int mon = weekSchedule.getDayScheduleList().stream()
                .filter(daySchedule -> daySchedule.getDay().equals("MON"))
                .findFirst().get().getBrokers().size();
        assertEquals(2, mon);
    }

    @Test
    public void shouldBeAbleToCreateAScheduleGiveShiftPlaceMONOnlyHasOneSpot() throws Exception {
        Broker broker1 = Broker.builder()
                .brokerId("bmon")
                .name("bomn-name")
                .preference(Preference.builder().weekDay("MON").build())
                .build();
        Broker broker11 = Broker.builder()
                .brokerId("bmon1")
                .name("bomn-name")
                .preference(Preference.builder().weekDay("MON").build())
                .build();
        Broker broker2 = Broker.builder()
                .brokerId("btue")
                .name("btue-name")
                .preference(Preference.builder().weekDay("TUE").build())
                .build();
        List<Broker> brokers = Arrays.asList(broker1, broker2, broker11);
        ShiftPlace shiftPlaceMON = ShiftPlace.builder()
                .days(Arrays.asList("MON"))
                .places("1")
                .managersName("MN")
                .shiftPlaceId("SP-MON")
                .build();
        ShiftPlace shiftPlaceTUE = ShiftPlace.builder()
                .days(Arrays.asList("TUE"))
                .places("1")
                .managersName("MN1")
                .shiftPlaceId("SP-TUE")
                .build();
        List<ShiftPlace> shiftPlaces = Arrays.asList(shiftPlaceMON, shiftPlaceTUE);

        WeekSchedule weekSchedule = buildWeekSchedule.buildDaySchedule(brokers, shiftPlaces);
        int mon = weekSchedule.getDayScheduleList().stream()
                .filter(daySchedule -> daySchedule.getDay().equals("MON"))
                .findFirst().get().getBrokers().size();
        assertEquals(1, mon);
    }

    @Test
    public void shouldBeAbleToCreateAScheduleGiveHasMoreBrokersThanSpot() throws Exception {
        Broker broker1 = Broker.builder()
                .brokerId("bmon")
                .name("bomn-name")
                .preference(Preference.builder().weekDay("MON").build())
                .build();
        Broker broker11 = Broker.builder()
                .brokerId("bmon1")
                .name("bomn-name")
                .preference(Preference.builder().weekDay("MON").build())
                .build();
        Broker broker2 = Broker.builder()
                .brokerId("btue")
                .name("btue-name")
                .preference(Preference.builder().weekDay("TUE").build())
                .build();
        List<Broker> brokers = Arrays.asList(broker1, broker2, broker11);
        ShiftPlace shiftPlaceMON = ShiftPlace.builder()
                .days(Arrays.asList("MON"))
                .places("1")
                .managersName("MN")
                .shiftPlaceId("SP-MON")
                .build();
        List<ShiftPlace> shiftPlaces = Arrays.asList(shiftPlaceMON);

        WeekSchedule weekSchedule = buildWeekSchedule.buildDaySchedule(brokers, shiftPlaces);
        int mon = weekSchedule.getDayScheduleList().stream()
                .filter(daySchedule -> daySchedule.getDay().equals("MON"))
                .findFirst().get().getBrokers().size();
        assertEquals(1, mon);

        DaySchedule tue = weekSchedule.getDayScheduleList().stream()
                .filter(daySchedule -> daySchedule.getDay().equals("TUE"))
                .findFirst().get();
        assertNull(tue.getBrokers());
    }

    @Test
    public void shouldBeAbleToCreateAScheduleWIthAShiftPlaceWithMoreTehnAPreferences() throws Exception {
        Broker broker1 = Broker.builder()
                .brokerId("bmon")
                .name("bomn-name")
                .preference(Preference.builder().weekDay("MON").build())
                .build();
        Broker broker11 = Broker.builder()
                .brokerId("bmon1")
                .name("bomn-name")
                .preference(Preference.builder().weekDay("MON").build())
                .build();
        Broker broker2 = Broker.builder()
                .brokerId("btue")
                .name("btue-name")
                .preference(Preference.builder().weekDay("TUE").build())
                .build();
        List<Broker> brokers = Arrays.asList(broker1, broker2, broker11);
        ShiftPlace shiftPlaceMON = ShiftPlace.builder()
                .days(Arrays.asList("MON"))
                .places("3")
                .managersName("MN")
                .shiftPlaceId("SP-MON")
                .build();
        ShiftPlace shiftPlaceTUE = ShiftPlace.builder()
                .days(Arrays.asList("TUE"))
                .places("2")
                .managersName("MN1")
                .shiftPlaceId("SP-TUE")
                .build();
        List<ShiftPlace> shiftPlaces = Arrays.asList(shiftPlaceMON, shiftPlaceTUE);

        WeekSchedule weekSchedule = buildWeekSchedule.buildDaySchedule(brokers, shiftPlaces);
        int mon = weekSchedule.getDayScheduleList().stream()
                .filter(daySchedule -> daySchedule.getDay().equals("MON"))
                .findFirst().get().getBrokers().size();
        assertEquals(3, mon);

        int tue = weekSchedule.getDayScheduleList().stream()
                .filter(daySchedule -> daySchedule.getDay().equals("TUE"))
                .findFirst().get().getBrokers().size();
        assertEquals(2, tue);
    }

    @Test
    public void shouldBeAbleToCreateAScheduleGivenMOreSpotOnShiftPlaceThanBrokers() throws Exception {
        Broker broker1 = Broker.builder()
                .brokerId("bmon")
                .name("bomn-name")
                .preference(Preference.builder().weekDay("MON").build())
                .build();
        Broker broker11 = Broker.builder()
                .brokerId("bmon1")
                .name("bomn-name")
                .preference(Preference.builder().weekDay("MON").build())
                .build();
        Broker broker2 = Broker.builder()
                .brokerId("btue")
                .name("btue-name")
                .preference(Preference.builder().weekDay("TUE").build())
                .build();
        List<Broker> brokers = Arrays.asList(broker1, broker2, broker11);
        ShiftPlace shiftPlaceMON = ShiftPlace.builder()
                .days(Arrays.asList("MON"))
                .places("4")
                .managersName("MN")
                .shiftPlaceId("SP-MON")
                .build();
        ShiftPlace shiftPlaceTUE = ShiftPlace.builder()
                .days(Arrays.asList("TUE"))
                .places("2")
                .managersName("MN1")
                .shiftPlaceId("SP-TUE")
                .build();
        List<ShiftPlace> shiftPlaces = Arrays.asList(shiftPlaceMON, shiftPlaceTUE);

        WeekSchedule weekSchedule = buildWeekSchedule.buildDaySchedule(brokers, shiftPlaces);
        int mon = weekSchedule.getDayScheduleList().stream()
                .filter(daySchedule -> daySchedule.getDay().equals("MON"))
                .findFirst().get().getBrokers().size();
        assertEquals(3, mon);

        int tue = weekSchedule.getDayScheduleList().stream()
                .filter(daySchedule -> daySchedule.getDay().equals("TUE"))
                .findFirst().get().getBrokers().size();
        assertEquals(2, tue);
    }
}