package com.broker.scheduler.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/schedule")
public class ScheduleController {


    @RequestMapping(method = GET)
    public ScheduleDTO fetchSchedule() {
        return new ScheduleDTO();
    }

    private class ScheduleDTO {
        private String id;
        private String manager;
    }
}
