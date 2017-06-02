package com.broker.scheduler.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/schedule")
public class ScheduleController {


    @RequestMapping(method = GET)
    public ScheduleDTO fetchSchedule() {
        return new ScheduleDTO();
    }

    @RequestMapping(method = POST,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public SchedulerWrapper createSchedule() {
        return new SchedulerWrapper();
    }

    private class ScheduleDTO {
        private String id;
        private String manager;
    }

    private class SchedulerWrapper {
    }
}
