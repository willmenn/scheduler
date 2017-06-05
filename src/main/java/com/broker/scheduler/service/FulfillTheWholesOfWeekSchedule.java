package com.broker.scheduler.service;

import com.broker.scheduler.model.Broker;
import com.broker.scheduler.model.DaySchedule;
import com.broker.scheduler.model.ShiftPlace;
import com.broker.scheduler.model.WeekSchedule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FulfillTheWholesOfWeekSchedule {


    public WeekSchedule fulfillTheWhole(WeekSchedule weekSchedule,
                                        List<Broker> brokers,
                                        List<ShiftPlace> shiftPlaces) {

        weekSchedule.getDayScheduleList().stream()
                .filter(daySchedule -> daySchedule.getBrokers() != null)
                .forEach((DaySchedule daySchedule) -> {

                    List<Broker> brokerList = brokers
                            .stream()
                            .filter(broker -> !daySchedule.getBrokers().contains(broker))
                            .collect(Collectors.toList());

                    ShiftPlace shiftPlace1 = shiftPlaces.stream()
                            .filter(shiftPlace ->
                                    shiftPlace.getShiftPlaceId().equals(daySchedule.getPlaceId()))
                            .findFirst().get();

                    int places = Integer.parseInt(shiftPlace1.getPlaces());
                    int missingPlaces = places - daySchedule.getBrokers().size();
                    int numberOfMissingBrokers = missingPlaces < brokerList.size() ? missingPlaces : brokerList.size();
                    for (int i = 0; i < numberOfMissingBrokers; i++) {
                        daySchedule.getBrokers().add(brokerList.get(i));
                    }
                });

        return weekSchedule;
    }
}
