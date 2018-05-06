package com.broker.scheduler.service.v3.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by wahrons on 29/04/18.
 */
public class ArrayUtils {

    public static List shuffleArray(List<Schedule.BrokerV3> list, RandomScheduler randomNumber) {
        Map<String,Schedule.BrokerV3> v3 = new HashMap<>();
        Set<Integer> integerRandom = new TreeSet<>();

        while (v3.size() != list.size()) {
            int randomInt = randomNumber.getRandomInt(list.size()) + randomNumber.getRandomInt(list.size()) + list.size() / list.size();
            if (integerRandom.contains(randomInt)) {
                continue;
            }
            if (randomInt >= list.size()) {
                randomInt = randomNumber.getRandomInt(list.size());
            }
            v3.put(list.get(randomInt).getName(),list.get(randomInt));
            integerRandom.add(randomInt);
        }

        return new ArrayList<>(v3.values());
    }

    public static List shuffleArraySP(List<Schedule.ShiftPlaceV3> list, RandomScheduler randomNumber) {
        Map<String,Schedule.ShiftPlaceV3> v3 = new HashMap<>();
        Set<Integer> integerRandom = new TreeSet<>();

        while (v3.size() != list.size()) {
            int randomInt = randomNumber.getRandomInt(list.size());
            if (integerRandom.contains(randomInt)) {
                continue;
            }
            v3.put(list.get(randomInt).getName(),list.get(randomInt));
            integerRandom.add(randomInt);
        }

        return new ArrayList<>(v3.values());
    }
}
