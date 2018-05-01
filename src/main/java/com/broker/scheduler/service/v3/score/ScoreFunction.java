package com.broker.scheduler.service.v3.score;

import com.broker.scheduler.service.v3.model.DayEnum;
import com.broker.scheduler.service.v3.model.ShiftTimeEnum;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.function.BiFunction;

/**
 * Created by wahrons on 30/04/18.
 */
@Getter(AccessLevel.PACKAGE)
public enum ScoreFunction {

    SHIFT_PLACE((name, brokerScore) -> brokerScore.getShiftPlaces().containsKey(name) ? 2 : 0),
    PARTIAL_SHIFT_PLACE((name, brokerScore) -> brokerScore.getShiftPlaces().containsKey(name) ? 1 : 0),
    DAY((name, brokerScore) -> brokerScore.getDays().containsKey(DayEnum.valueOf(name)) ? 2 : 0),
    PARTIAL_DAY((name, brokerScore) -> brokerScore.getDays().containsKey(DayEnum.valueOf(name)) ? 1 : 0),
    SHIFT((name, brokerScore) -> brokerScore.getShifts().containsKey(ShiftTimeEnum.valueOf(name)) ? 2 : 0),
    PARTIAL_SHIFT((name, brokerScore) -> brokerScore.getShifts().containsKey(ShiftTimeEnum.valueOf(name)) ? 1 : 0);

    private BiFunction<String, BrokerScore, Integer> constraint;

    ScoreFunction(BiFunction<String, BrokerScore, Integer> constraint) {
        this.constraint = constraint;
    }
}
