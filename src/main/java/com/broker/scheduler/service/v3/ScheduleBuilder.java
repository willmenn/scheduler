package com.broker.scheduler.service.v3;

import com.broker.scheduler.service.v3.model.AlreadyScheduled;
import com.broker.scheduler.service.v3.model.DayEnum;
import com.broker.scheduler.service.v3.model.RandomScheduler;
import com.broker.scheduler.service.v3.model.Schedule;

import java.util.List;


/**
 * Created by wahrons on 29/04/18.
 */
public class ScheduleBuilder {

    public Schedule createSchedule(Schedule schedule,
                                   AlreadyScheduled alreadyScheduled,
                                   RandomScheduler randomNumber) {
        List<Schedule.BrokerV3> brokerV3List = schedule.getBrokerV3s();
        schedule.getShiftPlaceV3List(randomNumber).forEach(sp -> {
            sp.getDays().entrySet().forEach(entry -> {
                addBrokersToShiftTimeOfADay(brokerV3List, alreadyScheduled,
                        entry.getValue().getName(), entry.getValue().getMorning(),
                        randomNumber);
                addBrokersToShiftTimeOfADay(brokerV3List, alreadyScheduled,
                        entry.getValue().getName(), entry.getValue().getAfternoon(),
                        randomNumber);
                addBrokersToShiftTimeOfADay(brokerV3List, alreadyScheduled,
                        entry.getValue().getName(), entry.getValue().getNight(),
                        randomNumber);
            });
        });

        return schedule;
    }

    private void addBrokersToShiftTimeOfADay(List<Schedule.BrokerV3> brokerV3List,
                                             AlreadyScheduled alreadyScheduled,
                                             DayEnum dayEnum,
                                             Schedule.Shift shift, RandomScheduler randomNumber) {
        List<Schedule.BrokerV3> brokersFree = alreadyScheduled.getUpdatedBrokerList(dayEnum,
                shift.getName(), brokerV3List, randomNumber);
        int count = 0;
        int max = shift.getMax() <= (brokersFree.size()) ? shift.getMax() : (brokersFree.size());
        while (max > count) {
            shift.getBrokerV3List().add(brokersFree.get(count));
            alreadyScheduled.addBroker(dayEnum, shift.getName(), brokersFree.get(count));
            count++;
        }
    }

}
