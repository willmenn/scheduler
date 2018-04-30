package com.broker.scheduler.service.v3.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by wahrons on 29/04/18.
 */
public class ArrayUtils {

    public static List shuffleArray(List list, RandomScheduler randomNumber) {
        List v3 = new ArrayList<>(list.size());
        Set<Integer> integerRandom = new TreeSet<>();

        while (v3.size() != list.size()) {
            int randomInt = randomNumber.getRandomInt(list.size());
            if (integerRandom.contains(randomInt)) {
                continue;
            }
            v3.add(list.get(randomInt));
            integerRandom.add(randomInt);
        }

        return v3;
    }
}
