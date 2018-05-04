package com.broker.scheduler.service.v3.score;

import com.broker.scheduler.service.v3.model.DayEnum;
import com.broker.scheduler.service.v3.model.Schedule;
import com.broker.scheduler.service.v3.model.Schedule.Day;
import com.broker.scheduler.service.v3.model.Schedule.Shift;
import com.broker.scheduler.service.v3.model.Schedule.ShiftPlaceV3;
import com.broker.scheduler.service.v3.model.ShiftTimeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wahrons on 30/04/18.
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
class BrokerScore {
    private String name;
    private Map<String, ShiftPlaceV3> shiftPlaces;
    private Map<DayEnum, Day> days;
    private Map<ShiftTimeEnum, Shift> shifts;
    private Map<ScoreFunction, List<String>> constraints;
    private Schedule.BrokerV3 brokerV3;
    private int score;

    Map<String, BrokerScore> buildBrokerScoreMap(Schedule schedule) {
        Map<String, BrokerScore> brokerScore = BrokerScore.createMapFromBrokerV3(schedule.getBrokerV3s());

        addValuesToBrokerScoreMap(schedule, brokerScore);

        return brokerScore;
    }

    int calculateScore() {
        int sum = this.constraints.entrySet().stream()
                .mapToInt((entry) ->
                        entry.getValue().stream()
                                .mapToInt(string ->
                                        entry.getKey().getConstraint()
                                                .apply(string, this)).sum())
                .sum();
        
        this.score = sum;
        addScoreToBrokerV3(sum);
        return sum;
    }

    private void addScoreToBrokerV3(int sum) {
        if(this.brokerV3.getScore() == null){
            this.brokerV3.setScore(BigDecimal.valueOf(sum));
        }else {
            this.brokerV3.setScore(this.brokerV3.getScore().add(BigDecimal.valueOf(sum)));
        }
    }

    private void addValuesToBrokerScoreMap(Schedule schedule, Map<String, BrokerScore> brokerScore) {
        schedule.getShiftPlaceV3List().forEach(sp -> sp.getDays().forEach((k, v) -> {
            putBrokerScore(brokerScore, sp, v, v.getMorning());
            putBrokerScore(brokerScore, sp, v, v.getAfternoon());
            putBrokerScore(brokerScore, sp, v, v.getNight());
        }));
    }

    private static Map<String, BrokerScore> createMapFromBrokerV3(List<Schedule.BrokerV3> v3) {
        Map<String, BrokerScore> scores = new HashMap<>(v3.size());
        v3.forEach(broker -> scores.put(broker.getName(), BrokerScore.builder()
                .name(broker.getName())
                .shiftPlaces(new HashMap<>())
                .days(new HashMap<>())
                .shifts(new HashMap<>())
                .constraints(broker.getConstraints())
                .brokerV3(broker)
                .build()));
        return scores;
    }

    private BrokerScore putShiftPlaces(ShiftPlaceV3 shiftPlaceV3) {
        if (!this.shiftPlaces.containsKey(shiftPlaceV3.getName())) {
            this.shiftPlaces.put(shiftPlaceV3.getName(), shiftPlaceV3);
        }
        return this;
    }

    private BrokerScore putDay(Day day) {
        if (!this.days.containsKey(day.getName())) {
            this.days.put(day.getName(), day);
        }
        return this;
    }

    private BrokerScore putShift(Shift shift) {
        if (!this.shifts.containsKey(shift.getName())) {
            this.shifts.put(shift.getName(), shift);
        }
        return this;
    }

    private void putBrokerScore(Map<String, BrokerScore> brokerScore, ShiftPlaceV3 shiftPlaceV3,
                                Day day, Shift shift) {
        shift.getBrokerV3List()
                .forEach(brokerV3 -> brokerScore.get(brokerV3.getName())
                        .putShiftPlaces(shiftPlaceV3).putDay(day)
                        .putShift(shift));
    }
}