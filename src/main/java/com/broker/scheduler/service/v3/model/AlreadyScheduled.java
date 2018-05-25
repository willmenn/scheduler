package com.broker.scheduler.service.v3.model;

import com.broker.scheduler.service.v3.model.Schedule.BrokerV3;
import com.broker.scheduler.service.v3.model.Schedule.Day;
import com.broker.scheduler.service.v3.model.Schedule.Shift;
import com.broker.scheduler.service.v3.score.ScoreFunction;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import static com.broker.scheduler.service.v3.score.ScoreFunction.DAY;
import static com.broker.scheduler.service.v3.score.ScoreFunction.SHIFT;
import static com.broker.scheduler.service.v3.score.ScoreFunction.SHIFT_PLACE;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

/**
 * Created by wahrons on 29/04/18.
 */
@Slf4j
public class AlreadyScheduled {
    Map<DayEnum, Day> alreadyScheduled;

    public AlreadyScheduled() {
        this.alreadyScheduled = new HashMap<>();
        this.alreadyScheduled.put(SUN, new Day(SUN));
        this.alreadyScheduled.put(MON, new Day(MON));
        this.alreadyScheduled.put(TUE, new Day(TUE));
        this.alreadyScheduled.put(WED, new Day(WED));
        this.alreadyScheduled.put(THU, new Day(THU));
        this.alreadyScheduled.put(FRI, new Day(FRI));
        this.alreadyScheduled.put(SAT, new Day(SAT));
    }

    public boolean containsBrokerOnDayShift(DayEnum day, ShiftTimeEnum shiftName, String broker) {
        Shift shift = getShiftForADay(day, shiftName);

        return shift.getBrokerV3List().stream()
                .anyMatch(b -> b.getName().equals(broker));
    }

    public List<BrokerV3> getUpdatedBrokerList(DayEnum day,
                                               ShiftTimeEnum shiftTimeEnum,
                                               List<BrokerV3> brokers,
                                               RandomScheduler randomNumber,
                                               Schedule.ShiftPlaceV3 sp) {
        List<BrokerV3> brokersFiltered = brokers.stream()
                .filter(broker ->
                        !this.containsBrokerOnDayShift(day, shiftTimeEnum, broker.getName()))
                .filter(broker -> broker.getConstraints() == null || (broker.getConstraints() != null &&
                        broker.getConstraints().getOrDefault(DAY, newArrayList("NONE")).stream()
                        .noneMatch(d -> d.equals(day.name()))))
                .filter(broker -> broker.getConstraints() == null ||
                        (broker.getConstraints() != null && broker.getConstraints()
                        .getOrDefault(SHIFT, newArrayList("NONE")).stream()
                        .noneMatch(s -> s.equals(shiftTimeEnum.name()))))
                .filter(broker -> broker.getConstraints() == null ||
                        (broker.getConstraints() != null &&
                        broker.getConstraints().getOrDefault(SHIFT_PLACE, newArrayList("NONE"))
                        .stream().noneMatch(s -> s.equals(sp.getName()))))
                .collect(toList());

        Collections.shuffle(brokersFiltered);
        return brokersFiltered;
    }

    public void addBroker(DayEnum day, ShiftTimeEnum shiftName, BrokerV3 broker) {

        Shift shift = getShiftForADay(day, shiftName);
        shift.getBrokerV3List().add(broker);
    }

    private Shift getShiftForADay(DayEnum day, ShiftTimeEnum shiftName) {
        Day dayObject = this.alreadyScheduled.get(day);
        return getShift(shiftName, dayObject);
    }

    private Shift getShift(ShiftTimeEnum shiftName, Day dayObject) {
        if (shiftName.equals(MORNING)) {
            return dayObject.getMorning();
        } else if (shiftName.equals(AFTERNOON)) {
            return dayObject.getAfternoon();
        } else if (shiftName.equals(NIGHT)) {
            return dayObject.getNight();
        }
        return dayObject.getMorning();
    }

    public void removeBrokers(List<BrokerV3> brokerV3s) {
        this.alreadyScheduled.entrySet().forEach(entry -> {
            entry.getValue().getMorning().removeBrokers(brokerV3s);
            entry.getValue().getAfternoon().removeBrokers(brokerV3s);
            entry.getValue().getNight().removeBrokers(brokerV3s);
        });
    }

    public boolean removeBrokerFromDayShift(BrokerV3 removed, Map.Entry<DayEnum, Day> firstDay, Shift shift) {
        if (!this.alreadyScheduled.containsKey(firstDay.getKey())) {
        //    log.info("Already Schedule -" + firstDay.getValue().getName().name());
            return false;
        }

        Shift s = null;
        if (shift.getName().equals(MORNING)) {
            s = this.alreadyScheduled.get(firstDay.getKey())
                    .getMorning();
        } else if (shift.getName().equals(AFTERNOON)) {
            s = this.alreadyScheduled.get(firstDay.getKey())
                    .getAfternoon();
        } else if (shift.getName().equals(NIGHT)) {
            s = this.alreadyScheduled.get(firstDay.getKey())
                    .getNight();
        }
        //log.info("Already Schedule -"+s.getName().name());
        if (s != null) {
            s.removeBrokers(newArrayList(removed));
       //     log.info("Already Schedule 2 -"+s.getName().name());
            return true;
        }
        return false;
    }
}
