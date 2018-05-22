package com.broker.scheduler.service.v3.score;

import com.broker.scheduler.service.v3.model.DayEnum;
import com.broker.scheduler.service.v3.model.ShiftTimeEnum;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.function.BiFunction;

/**
 * Created by wahrons on 30/04/18.
 */
@Getter(AccessLevel.PUBLIC)
public enum ScoreFunction {

    SHIFT_PLACE((name, brokerScore) -> brokerScore.getShiftPlaces().containsKey(name)
            ? brokerScore.getShiftPlaces().get(name) * 2 : 0),
    PARTIAL_SHIFT_PLACE((name, brokerScore) -> brokerScore.getShiftPlaces().getOrDefault(name, 0)),
    DAY((name, brokerScore) -> brokerScore.getDays().containsKey(DayEnum.valueOf(name))
            ? brokerScore.getDays().get(DayEnum.valueOf(name)) * 2 : 0),
    PARTIAL_DAY((name, brokerScore) -> brokerScore.getDays().getOrDefault(DayEnum.valueOf(name), 0)),
    SHIFT((name, brokerScore) -> brokerScore.getShifts().containsKey(ShiftTimeEnum.valueOf(name))
            ? brokerScore.getShifts().get(ShiftTimeEnum.valueOf(name)) * 2 : 0),
    PARTIAL_SHIFT((name, brokerScore) -> brokerScore.getShifts().getOrDefault(ShiftTimeEnum.valueOf(name), 0));

    private BiFunction<String, BrokerScore, Integer> constraint;

    ScoreFunction(BiFunction<String, BrokerScore, Integer> constraint) {
        this.constraint = constraint;
    }
}
