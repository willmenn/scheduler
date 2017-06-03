package com.broker.scheduler.model;


import com.google.common.base.Preconditions;
import lombok.Getter;

import java.util.List;

@Getter
public class WeekSchedule {
    private static final int NUMBER_OF_DAYS_ON_A_WEEK = 7;
    private List<DaySchedule> dayScheduleList;

    public WeekSchedule(List<DaySchedule> dayScheduleList) {
        Preconditions.checkNotNull(dayScheduleList);
        Preconditions.checkArgument(dayScheduleList.size() == NUMBER_OF_DAYS_ON_A_WEEK);
        this.dayScheduleList = dayScheduleList;
    }
}
