package com.broker.scheduler.service.v3;

import com.broker.scheduler.service.v3.model.AlreadyScheduled;
import com.broker.scheduler.service.v3.model.DayEnum;
import com.broker.scheduler.service.v3.model.RandomScheduler;
import com.broker.scheduler.service.v3.model.Schedule;

import java.math.BigDecimal;
import java.util.List;

import static java.math.BigDecimal.ZERO;


/**
 * Created by wahrons on 29/04/18.
 */
public class ScheduleBuilder {

    public Schedule createSchedule(Schedule schedule,
                                   AlreadyScheduled alreadyScheduled,
                                   RandomScheduler randomNumber) {
        List<Schedule.BrokerV3> brokerV3List = getBrokerV3sWithScoreZero(schedule);

        schedule.getShiftPlaceV3List(randomNumber).forEach(sp -> {
            sp.getDays().entrySet().forEach(entry -> {
                addBrokersToShiftTimeOfADay(brokerV3List, alreadyScheduled,
                        entry.getValue().getName(), entry.getValue().getMorning(),
                        randomNumber,sp);
                addBrokersToShiftTimeOfADay(brokerV3List, alreadyScheduled,
                        entry.getValue().getName(), entry.getValue().getAfternoon(),
                        randomNumber,sp);
                addBrokersToShiftTimeOfADay(brokerV3List, alreadyScheduled,
                        entry.getValue().getName(), entry.getValue().getNight(),
                        randomNumber,sp);
            });
        });

        return schedule;
    }

    private List<Schedule.BrokerV3> getBrokerV3sWithScoreZero(Schedule schedule) {
        List<Schedule.BrokerV3> brokerV3List = schedule.getBrokerV3s();
        brokerV3List.forEach(brokerV3 -> brokerV3.setScore(ZERO));
        return brokerV3List;
    }

    private void addBrokersToShiftTimeOfADay(List<Schedule.BrokerV3> brokerV3List,
                                             AlreadyScheduled alreadyScheduled,
                                             DayEnum dayEnum,
                                             Schedule.Shift shift, RandomScheduler randomNumber,
                                             Schedule.ShiftPlaceV3 sp) {
        List<Schedule.BrokerV3> brokersFree = alreadyScheduled.getUpdatedBrokerList(dayEnum,
                shift.getName(), brokerV3List, randomNumber, sp);
        int count = 0;
        int max = shift.getMax() <= (brokersFree.size()) ? shift.getMax() : (brokersFree.size());
        while (max > count) {
            shift.getBrokerV3List().add(brokersFree.get(count));
            alreadyScheduled.addBroker(dayEnum, shift.getName(), brokersFree.get(count));
            count++;
        }
    }

}
