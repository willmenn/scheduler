package com.broker.scheduler.service.v3.model;

import java.util.Random;

/**
 * Created by wahrons on 29/04/18.
 */
public class RandomNumber implements RandomScheduler {

    private Random random;

    public RandomNumber() {
        this.random = new Random();
    }

    @Override
    public int getRandomInt(int bound) {
        return random.nextInt(bound);
    }
}
