package com.broker.scheduler.service.v2;


import com.broker.scheduler.model.Broker;
import com.broker.scheduler.service.v2.model.Plantao;
import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Component
public class BuildMultiSchedule {

    private Map<String, List<Broker>> alreadyScheduled;

    public ScheduleWrapper build(List<Plantao> plantoes, List<Broker> brokers) {
        alreadyScheduled = Maps.newHashMap();
        buildEmptyScheduleMap();

        Map<String, List<Broker>> mapFromBrokersMatchingDays = createMapFromBrokersMatchingDays(brokers);

        List<Plantao> plantoesComPreferencia = plantoes.stream().map(plantao -> {
            Map<String, List<Broker>> scheduled = Maps.newHashMap();
            plantao.getDays().keySet().forEach(day -> {
                Integer positions = plantao.getDays().get(day);
                List<Broker> brokersList = mapFromBrokersMatchingDays.get(day);

                List<Broker> brokerForDay = scheduleBroker(day, positions, brokersList);

                if (brokerForDay.size() > 0) {
                    addAlreadyScheduledDay(alreadyScheduled, brokerForDay, day);
                    scheduled.put(day, brokerForDay);
                }
            });

            return plantao.toBuilder().scheduled(filScheduleWithEmptyDays(scheduled)).build();

        }).collect(toList());

        return ScheduleWrapper.builder()
                .plantaos(plantoesComPreferencia)
                .alreadyScheduled(alreadyScheduled)
                .build();

    }

    private Map<String, List<Broker>> filScheduleWithEmptyDays(Map<String, List<Broker>> scheduled) {
        List<String> days = asList("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT");

        days.stream()
                .filter(day -> scheduled.get(day) == null)
                .forEach(day -> scheduled.put(day, newArrayList()));

        return scheduled;
    }

    private List<Broker> scheduleBroker(String day, Integer positions, List<Broker> brokersList) {
        List<Broker> brokerForDay = newArrayList();
        for (int i = 0; i < positions; i++) {
            if (brokerListIsValid(brokersList, i)
                    && brokerNotScheduled(brokersList.get(i), day, alreadyScheduled)) {
                brokerForDay.add(brokersList.get(i));
            }
        }
        return brokerForDay;
    }

    private boolean brokerListIsValid(List<Broker> brokersList, int i) {
        return brokersList != null && brokersList.size() > i;
    }

    private void addAlreadyScheduledDay(Map<String, List<Broker>> alreadyScheduled, List<Broker> brokerForDay,
                                        String day) {
        alreadyScheduled.get(day).addAll(brokerForDay);
    }

    private boolean brokerNotScheduled(Broker broker, String day, Map<String, List<Broker>> alreadyScheduled) {
        return alreadyScheduled.get(day).stream().noneMatch(broker1 -> broker.equals(broker1));
    }

    private Map<String, List<Broker>> createMapFromBrokersMatchingDays(List<Broker> brokers) {
        return brokers.stream()
                .collect(Collectors
                        .groupingBy(brokerMap -> brokerMap.getPreference().getWeekDay()));
    }

    private void buildEmptyScheduleMap() {
        alreadyScheduled.put("SUN", newArrayList());
        alreadyScheduled.put("MON", newArrayList());
        alreadyScheduled.put("TUE", newArrayList());
        alreadyScheduled.put("WED", newArrayList());
        alreadyScheduled.put("THU", newArrayList());
        alreadyScheduled.put("FRI", newArrayList());
        alreadyScheduled.put("SAT", newArrayList());
    }



    @Data
    @Builder(toBuilder = true)
    @ToString
    public static class ScheduleWrapper {
        List<Plantao> plantaos;
        Map<String, List<Broker>> alreadyScheduled;
    }
}
