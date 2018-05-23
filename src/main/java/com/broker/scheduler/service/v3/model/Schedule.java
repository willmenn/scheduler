package com.broker.scheduler.service.v3.model;

import com.broker.scheduler.model.Broker;
import com.broker.scheduler.service.v2.model.Plantao;
import com.broker.scheduler.service.v3.score.ScoreFunction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.broker.scheduler.service.v3.model.DayEnum.FRI;
import static com.broker.scheduler.service.v3.model.DayEnum.MON;
import static com.broker.scheduler.service.v3.model.DayEnum.SAT;
import static com.broker.scheduler.service.v3.model.DayEnum.SUN;
import static com.broker.scheduler.service.v3.model.DayEnum.THU;
import static com.broker.scheduler.service.v3.model.DayEnum.TUE;
import static com.broker.scheduler.service.v3.model.DayEnum.WED;
import static com.broker.scheduler.service.v3.model.ShiftTimeEnum.AFTERNOON;
import static com.broker.scheduler.service.v3.model.ShiftTimeEnum.MORNING;
import static com.broker.scheduler.service.v3.model.ShiftTimeEnum.NIGHT;
import static java.util.stream.Collectors.toList;

/**
 * Created by wahrons on 29/04/18.
 */
@Data
@AllArgsConstructor
@Document(collection = "schedulerV3")
@Builder(toBuilder = true)
public class Schedule {
    @Id
    private String id;
    private String name;
    private List<ShiftPlaceV3> shiftPlaceV3List;
    private String managerName;
    private LocalDateTime createdTimestamp;
    private List<BrokerV3> brokerV3s;
    private BigDecimal score;
    private Map<String, Set<Day>> brokerSchedule;

    public Map<String, Set<Day>> getBrokerSchedule() {
        Map<String, Set<Day>> brokerMap = new HashMap<>();
        this.brokerV3s.stream().forEach(brokerV3 -> brokerMap.put(brokerV3.getName(), new TreeSet<>()));
        brokerMap.forEach((b, days) -> {
            this.shiftPlaceV3List
                    .forEach(sp -> sp.getDays().forEach((k, v) -> {
                        if (v.getMorning().getBrokerV3List().stream().anyMatch(broker -> broker.getName().equals(b))) {
                            Day day = brokerMap.get(b)
                                    .stream()
                                    .filter(d -> d.getName().equals(k))
                                    .findFirst()
                                    .orElse(new Day(k));
                            day.getMorning().getShiftPlaceV3s().add(sp);
                            brokerMap.get(b).add(day);
                        }
                        if (v.getAfternoon().getBrokerV3List().stream().anyMatch(broker -> broker.getName().equals(b))) {
                            Day day = brokerMap.get(b)
                                    .stream()
                                    .filter(d -> d.getName().equals(k))
                                    .findFirst()
                                    .orElse(new Day(k));
                            day.getAfternoon().getShiftPlaceV3s().add(sp);
                            brokerMap.get(b).add(day);
                        }
                        if (v.getNight().getBrokerV3List().stream().anyMatch(broker -> broker.getName().equals(b))) {
                            Day day = brokerMap.get(b)
                                    .stream()
                                    .filter(d -> d.getName().equals(k))
                                    .findFirst()
                                    .orElse(new Day(k));
                            day.getNight().getShiftPlaceV3s().add(sp);
                            brokerMap.get(b).add(day);
                        }
                    }));
        });
        return brokerMap;
    }

    public List<ShiftPlaceV3> getShiftPlaceV3List(RandomScheduler randomNumber) {
        // ArrayUtils.shuffleArraySP(this.shiftPlaceV3List, randomNumber);
        Collections.shuffle(this.shiftPlaceV3List);
        return this.shiftPlaceV3List;
    }

    public Schedule() {
        this.shiftPlaceV3List = new ArrayList<>();
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ShiftPlaceV3 implements Comparable<ShiftPlaceV3> {
        private String name;
        private String id;
        private Map<DayEnum, Day> days;

        public ShiftPlaceV3(String name, String id) {
            this.name = name;
            this.id = id;
            this.days = new HashMap<>(7);
            days.put(SUN, new Day(SUN));
            days.put(MON, new Day(MON));
            days.put(TUE, new Day(TUE));
            days.put(WED, new Day(WED));
            days.put(THU, new Day(THU));
            days.put(FRI, new Day(FRI));
            days.put(SAT, new Day(SAT));
        }

        @Override
        public int compareTo(ShiftPlaceV3 o) {
            return o.name.compareTo(this.name);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Day implements Comparable<Day> {
        private DayEnum name;
        private Shift morning;
        private Shift afternoon;
        private Shift night;
        private int placeLeftCount;

        public Day(DayEnum name) {
            this.name = name;
            this.morning = new Shift(MORNING);
            this.afternoon = new Shift(AFTERNOON);
            this.night = new Shift(NIGHT);
        }

        public int getPlaceLeftCount(){
            int morningCount = this.morning.getMax() - this.morning.getBrokerV3List().size();
            int afternoonCount = this.afternoon.getMax() - this.afternoon.getBrokerV3List().size();
            int nightCount = this.night.getMax() - this.night.getBrokerV3List().size();
            return morningCount + afternoonCount + nightCount;
        }

        @Override
        public int compareTo(Day o) {
            return o.getName().name().compareTo(this.name.name());
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class Shift {
        private ShiftTimeEnum name;
        private List<BrokerV3> brokerV3List;
        private List<ShiftPlaceV3> shiftPlaceV3s;
        private int max;

        public Shift(ShiftTimeEnum name) {
            this.brokerV3List = new ArrayList();
            this.shiftPlaceV3s = new ArrayList<>();
            this.name = name;
        }

        public List<BrokerV3> removeBroker(Double threshold) {
            List<BrokerV3> newList = this.brokerV3List.stream()
                    .filter(b -> b.getScore().compareTo(BigDecimal.valueOf(threshold)) < 0)
                    .collect(toList());
            List<BrokerV3> removed = this.brokerV3List.stream()
                    .filter(b -> b.getScore().compareTo(BigDecimal.valueOf(threshold)) >= 0)
                    .collect(toList());
            this.brokerV3List = newList;
            return removed;
        }

        public void removeBrokers(List<BrokerV3> brokerV3s) {
            this.brokerV3List = this.brokerV3List.stream()
                    .filter(brokerV3 -> brokerV3s.stream()
                            .noneMatch(b -> b.getName().equals(brokerV3.getName())))
                    .collect(toList());
        }
    }

    @Data
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BrokerV3 implements Comparable<BrokerV3> {
        private String name;
        private String id;
        @Setter
        private BigDecimal score;
        private Map<ScoreFunction, List<String>> constraints;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BrokerV3 brokerV3 = (BrokerV3) o;
            return Objects.equals(name, brokerV3.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public int compareTo(BrokerV3 o) {
            return o.getName().compareTo(this.name);
        }
    }

    public Schedule convertShiftPlaceToSchedule(List<Plantao> shiftPlaces) {
        Schedule schedule = new Schedule();
        shiftPlaces.forEach(sp -> {
            Schedule.ShiftPlaceV3 spV3 = new Schedule.ShiftPlaceV3(sp.getName(), sp.getShiftPlaceId());
            sp.getDaysV3().forEach((key, value) -> {
                Schedule.Day day = spV3.getDays().get(key);
                day.getMorning().setMax(value.getMorning());
                day.getAfternoon().setMax(value.getAfternoon());
                day.getNight().setMax(value.getNight());
            });
            schedule.getShiftPlaceV3List().add(spV3);
        });
        return schedule;
    }

    public Schedule setBrokers(List<Broker> brokers) {
        List<BrokerV3> v3 = new ArrayList<>(brokers.size());

        brokers.forEach(b -> v3.add(BrokerV3.builder()
                .id(b.getBrokerId())
                .name(b.getName())
                .constraints(b.getConstraints())
                .build()));

        this.setBrokerV3s(v3);
        return this;
    }

    public List<BrokerV3> removeAllBrokersForThreshold(Double threshold) {
        List<BrokerV3> brokerV3s = new ArrayList<>();
        this.shiftPlaceV3List.forEach(sp -> sp.getDays().entrySet()
                .forEach(day -> {
                    brokerV3s.addAll(day.getValue().morning.removeBroker(threshold));
                    brokerV3s.addAll(day.getValue().afternoon.removeBroker(threshold));
                    brokerV3s.addAll(day.getValue().night.removeBroker(threshold));
                }));

        return brokerV3s.stream().distinct().collect(toList());
    }
}
