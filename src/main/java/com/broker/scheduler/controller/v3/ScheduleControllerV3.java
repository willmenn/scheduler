package com.broker.scheduler.controller.v3;

import com.broker.scheduler.service.v3.ScheduleServiceV3;
import com.broker.scheduler.service.v3.model.Schedules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/v3")
public class ScheduleControllerV3 {


    private ScheduleServiceV3 serviceV3;

    @Autowired
    public ScheduleControllerV3(ScheduleServiceV3 serviceV3){
        this.serviceV3 = serviceV3;
    }

    @RequestMapping(method = POST, value = "/schedule",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public Schedules createSchedule(@RequestParam("manager") String manager) {
        return serviceV3.createSchedules(manager);
    }

}
