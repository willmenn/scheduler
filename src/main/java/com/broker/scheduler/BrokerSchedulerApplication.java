package com.broker.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BrokerSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrokerSchedulerApplication.class, args);
    }
}
