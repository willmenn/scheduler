package com.broker.scheduler.controller;

import com.broker.scheduler.model.ScheduleModel;
import com.broker.scheduler.model.WeekSchedule;
import com.broker.scheduler.service.v1.ScheduleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController

public class ScheduleController {

    private ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @RequestMapping(method = GET, value = "/schedule")
    public ScheduleDTO fetchSchedule(@RequestParam String id) {
        ScheduleModel scheduleModel = scheduleService.fetchSchduelById(id);
        return new ScheduleDTO(scheduleModel.getId(), scheduleModel.getManager(), scheduleModel.getWeekSchedule());
    }

    @RequestMapping(method = GET, value = "/schedule/broker")
    public String fetchSchedule(@RequestParam String id, @RequestParam String manager) {
        Map<String, List<String>> map = scheduleService.fetchScheduleByBroker(id, manager);
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return json;
    }

    @RequestMapping(method = POST, value = "/schedule",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public SchedulerWrapper createSchedule(@RequestBody @Valid ScheduleDTO dto) {
        ScheduleModel schedule = scheduleService.createSchedule(dto);
        SchedulerWrapper build = new SchedulerWrapper().build(schedule.getId());
        build.setScheduleId(schedule.getId());
        return build;
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

    @Setter
    @Getter
    private class SchedulerWrapper extends ResourceSupport {
        private String scheduleId;

        public SchedulerWrapper build(String id) {
            SchedulerWrapper schedulerWrapper = new SchedulerWrapper();
            schedulerWrapper.add(linkTo(methodOn(ScheduleController.class).fetchSchedule(id)).withSelfRel());
            return schedulerWrapper;
        }
    }

}
