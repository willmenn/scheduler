package com.broker.scheduler.controller;

import com.broker.scheduler.model.ScheduleModel;
import com.broker.scheduler.model.WeekSchedule;
import com.broker.scheduler.service.ScheduleService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/schedule")
public class ScheduleController {

    private ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @RequestMapping(method = GET)
    public ScheduleDTO fetchSchedule(@RequestParam String id) {
        ScheduleModel scheduleModel = scheduleService.fetchSchduelById(id);
        return new ScheduleDTO(scheduleModel.getId(), scheduleModel.getManager(), scheduleModel.getWeekSchedule());
    }

    @RequestMapping(method = POST,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public SchedulerWrapper createSchedule(@RequestBody @Valid ScheduleDTO dto) {
        ScheduleModel schedule = scheduleService.createSchedule(dto);
        return new SchedulerWrapper().build(schedule.getId());
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class ScheduleDTO {
        private String id;
        @NotNull
        private String manager;
        private WeekSchedule weekSchedule;
    }

    private class SchedulerWrapper extends ResourceSupport {
        public SchedulerWrapper build(String id) {
            SchedulerWrapper schedulerWrapper = new SchedulerWrapper();
            schedulerWrapper.add(linkTo(methodOn(ScheduleController.class).fetchSchedule(id)).withSelfRel());
            return schedulerWrapper;
        }
    }

}
