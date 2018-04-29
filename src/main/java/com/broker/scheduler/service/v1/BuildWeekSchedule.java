package com.broker.scheduler.service.v1;

import com.broker.scheduler.model.Broker;
import com.broker.scheduler.model.DaySchedule;
import com.broker.scheduler.model.ShiftPlace;
import com.broker.scheduler.model.WeekSchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;

@Component
public class BuildWeekSchedule {

    private FulfillTheWholesOfWeekSchedule fulfillTheWholesOfWeekSchedule;

    @Autowired
    public BuildWeekSchedule(FulfillTheWholesOfWeekSchedule fulfillTheWholesOfWeekSchedule) {
        this.fulfillTheWholesOfWeekSchedule = fulfillTheWholesOfWeekSchedule;
    }

    public WeekSchedule buildDaySchedule(List<Broker> brokers, List<ShiftPlace> shiftPlaces) {
        List<String> days = asList("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT");

        Map<String, List<ShiftPlace>> shiftPlaceMap = createMapFromShiftPlaceMatchingDays(shiftPlaces, days);

        Map<String, List<Broker>> brokersMap = createMapFromBrokersMatchingDays(brokers);

        MapWrapper mapWrapper = fulfillBrokerAndShiftMap(days, shiftPlaceMap, brokersMap);

        List<DaySchedule> daySchedules = buildDaySchedulesList(days, shiftPlaceMap, brokersMap, mapWrapper.brokersScheduled, mapWrapper.shiftPlaceMapUsed);

        WeekSchedule weekSchedule = new WeekSchedule(daySchedules);

        fulfillTheWholesOfWeekSchedule.fulfillTheWhole(weekSchedule, brokers, shiftPlaces);

        return weekSchedule;
    }

    @AllArgsConstructor
    @Getter
    class MapWrapper {
        Map<String, List<Broker>> brokersScheduled;
        Map<String, ShiftPlace> shiftPlaceMapUsed;
    }

    private MapWrapper fulfillBrokerAndShiftMap(List<String> days, Map<String, List<ShiftPlace>> shiftPlaceMap, Map<String, List<Broker>> brokersMap) {
        Map<String, List<Broker>> brokersScheduled = new HashMap<>();
        Map<String, ShiftPlace> shiftPlaceMapUsed = new HashMap<>();
        days.stream()
                .forEach(day -> {
                    if (shiftPlaceMap.containsKey(day) && brokersMap.containsKey(day)) {
                        ShiftPlace shiftPlace = shiftPlaceMap.get(day).stream().findFirst().get();
                        shiftPlaceMapUsed.put(day, shiftPlace);
                        int places = Integer.parseInt(shiftPlace.getPlaces());
                        List<Broker> brokersList = brokersMap.get(day);
                        List<Broker> brokerForDay = newArrayList();
                        for (int i = 0; i < places; i++) {
                            if (brokersList.size() > i) {
                                brokerForDay.add(brokersList.get(i));
                            }
                        }
                        brokersScheduled.put(day, brokerForDay);
                    }
                });
        return new MapWrapper(brokersScheduled, shiftPlaceMapUsed);
    }

    private List<DaySchedule> buildDaySchedulesList(List<String> days, Map<String, List<ShiftPlace>> shiftPlaceMap, Map<String, List<Broker>> brokersMap, Map<String, List<Broker>> brokersScheduled, Map<String, ShiftPlace> shiftPlaceMapUsed) {
        return days.stream().map(s -> {
            if (shiftPlaceMap.containsKey(s) && brokersMap.containsKey(s)) {
                ShiftPlace shiftPlace = shiftPlaceMapUsed.get(s);
                List<Broker> brokers1 = brokersScheduled.get(s);
                return new DaySchedule(shiftPlace.getShiftPlaceId(), s, brokers1);
            } else {
                return new DaySchedule(null, s, null);
            }
        }).collect(Collectors.toList());
    }

    private Map<String, List<Broker>> createMapFromBrokersMatchingDays(List<Broker> brokers) {
        return brokers.stream()
                .collect(Collectors
                        .groupingBy(brokerMap -> brokerMap.getPreference().getWeekDay()));
    }

    private Map<String, List<ShiftPlace>> createMapFromShiftPlaceMatchingDays(List<ShiftPlace> shiftPlaces, List<String> days) {
        return shiftPlaces.stream()
                .collect(Collectors.groupingBy(shiftPlace ->
                        shiftPlace.getDays().stream()
                                .filter(daysSP -> days.stream().anyMatch(s -> s.equals(daysSP)))
                                .findFirst().get()));
    }
}
