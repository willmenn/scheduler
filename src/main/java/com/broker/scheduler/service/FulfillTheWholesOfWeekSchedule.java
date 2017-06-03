package com.broker.scheduler.service;

import com.broker.scheduler.model.Broker;
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
                .forEach(daySchedule -> {
                    List<Broker> brokerList = daySchedule.getBrokers().stream().map(broker ->
                            brokers.stream().filter(broker1 -> !broker1.equals(broker))
                                    .collect(Collectors.toList())
                    ).findFirst().get();

                    ShiftPlace shiftPlace1 = shiftPlaces.stream()
                            .filter(shiftPlace ->
                                    shiftPlace.getShiftPlaceId().equals(daySchedule.getPlaceId()))
                            .findFirst().get();

                    int places = Integer.parseInt(shiftPlace1.getPlaces());
                    int numberOfmissingBrokers = places - daySchedule.getBrokers().size();
                    for (int i = 0; i < numberOfmissingBrokers; i++) {
                        daySchedule.getBrokers().add(brokerList.get(i));
                    }
                });

        return weekSchedule;
    }
}
