package com.broker.scheduler.service.v3.model;

import com.broker.scheduler.service.v3.model.Schedule.BrokerV3;
import com.broker.scheduler.service.v3.model.Schedule.Day;
import com.broker.scheduler.service.v3.model.Schedule.Shift;

import java.util.HashMap;
import java.util.Map;

import static com.broker.scheduler.service.v3.model.ShiftTimeEnum.AFTERNOON;
import static com.broker.scheduler.service.v3.model.ShiftTimeEnum.MORNING;
import static com.broker.scheduler.service.v3.model.ShiftTimeEnum.NIGHT;

/**
 * Created by wahrons on 29/04/18.
 */
public class AlreadyScheduled {
    Map<String, Day> alreadyScheduled;

    public AlreadyScheduled() {
        this.alreadyScheduled = new HashMap<>();
        this.alreadyScheduled.put("SUN", new Day("SUN"));
        this.alreadyScheduled.put("MON", new Day("MON"));
        this.alreadyScheduled.put("TUE", new Day("TUE"));
        this.alreadyScheduled.put("WED", new Day("WED"));
        this.alreadyScheduled.put("THU", new Day("THU"));
        this.alreadyScheduled.put("FRI", new Day("FRI"));
        this.alreadyScheduled.put("SAT", new Day("SAT"));
    }

    public boolean containsBrokerOnDayShift(String day, ShiftTimeEnum shiftName, String broker) {
        Shift shift = getShiftForADay(day, shiftName);

        return shift.getBrokerV3List().stream()
                .anyMatch(b -> b.getName().equals(broker));
    }

    public void addBroker(String day, ShiftTimeEnum shiftName, BrokerV3 broker) {
        Shift shift = getShiftForADay(day, shiftName);
        shift.getBrokerV3List().add(broker);
    }

    private Shift getShiftForADay(String day, ShiftTimeEnum shiftName) {
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
}
