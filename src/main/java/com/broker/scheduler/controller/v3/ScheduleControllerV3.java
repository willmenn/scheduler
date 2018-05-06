package com.broker.scheduler.controller.v3;

import com.broker.scheduler.repository.ScheduleV3Repository;
import com.broker.scheduler.service.v3.ScheduleServiceV3;
import com.broker.scheduler.service.v3.model.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/v3")
public class ScheduleControllerV3 {

    private ScheduleServiceV3 serviceV3;
    private ScheduleV3Repository repository;

    @Autowired
    public ScheduleControllerV3(ScheduleServiceV3 serviceV3,
                                ScheduleV3Repository repository) {
        this.serviceV3 = serviceV3;
        this.repository = repository;
    }

    @RequestMapping(method = POST, value = "/schedules",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public Schedule createSchedule(@RequestParam("manager") String manager) {
        return serviceV3.createSchedules(manager);
    }

    @RequestMapping(method = GET, value = "/schedules/{id}",
            produces = APPLICATION_JSON_VALUE)
    public Schedule fetchSchedule(@PathVariable("id") String id) {
        return repository.findOne(id);
    }

    @RequestMapping(method = GET, value = "/schedules/manager/{manager}",
            produces = APPLICATION_JSON_VALUE)
    public List<Schedule> fetchSchedulesByManager(@PathVariable("manager") String manager) {
        return repository.findAllByManagerName(manager);
    }
}
