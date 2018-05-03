package com.broker.scheduler.service.v3.helper;

import com.broker.scheduler.service.v2.model.Plantao;
import com.broker.scheduler.service.v3.ScheduleBuilder;
import com.broker.scheduler.service.v3.model.AlreadyScheduled;
import com.broker.scheduler.service.v3.model.DayEnum;
import com.broker.scheduler.service.v3.model.FakeRandomNumber;
import com.broker.scheduler.service.v3.model.Schedule;
import com.broker.scheduler.service.v3.score.ScoreFunction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.broker.scheduler.service.v3.model.DayEnum.MON;
import static com.google.common.collect.Lists.newArrayList;

public class ScheduleHelper {

    public static Schedule buildOnePlantaoMon345WithOneBroker(Map<ScoreFunction, List<String>> constraints){
        Map<DayEnum, Plantao.Shift> days = new HashMap<>();
        days.put(MON, new Plantao.Shift(3, 4, 5));

        Plantao plantao = Plantao.builder().name("n-1").daysV3(days).build();
        Schedule schedule = new Schedule().convertShiftPlaceToSchedule(newArrayList(plantao));

        Schedule.BrokerV3 brokerV3 = new Schedule.BrokerV3("John due", "1",null,constraints);
        schedule.setBrokerV3s(newArrayList(brokerV3));

        AlreadyScheduled alreadyScheduled = new AlreadyScheduled();
        ScheduleBuilder builder = new ScheduleBuilder();
        return builder.createSchedule(schedule, alreadyScheduled, new FakeRandomNumber());

    }
}
