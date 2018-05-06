package com.broker.scheduler.service.v3.model;

/**
 * Created by wahrons on 30/04/18.
 */
public class FakeRandomNumber implements RandomScheduler {

    private int count = 0;
    private int iterator = 0;

    @Override
    public int getRandomInt(int bound) {
        if (count >= bound) {
            count = 0;
            iterator = 0;
        }
        if (iterator > 0) {
            iterator++;
            return count++;
        }
        iterator++;
        return count;
    }
}
