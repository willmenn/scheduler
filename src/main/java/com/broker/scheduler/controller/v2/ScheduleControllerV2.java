package com.broker.scheduler.controller.v2;

import com.broker.scheduler.service.v2.ScheduleBrokerServiceV2;
import com.broker.scheduler.service.v2.model.ScheduleModelV2;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by wahrons on 15/01/18.
 */
@RestController
@RequestMapping(value = "/v2")
public class ScheduleControllerV2 {

    private ScheduleBrokerServiceV2 serviceV2;

    @Autowired
    public ScheduleControllerV2(ScheduleBrokerServiceV2 serviceV2) {
        this.serviceV2 = serviceV2;
    }

    @RequestMapping(method = POST, value = "/schedule",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ScheduleModelV2 createSchedule(@RequestBody @Valid ScheduleV2DTO dto) {
        return serviceV2.buildSchedule(dto.getManager());
    }

    @RequestMapping(method = GET, value = "/schedule")
    public ScheduleModelV2 getSchedule(@RequestParam("id") String id) {
        return serviceV2.getScheduleV2(id);
    }

    @RequestMapping(method = GET, value = "/schedules")
    public List<ScheduleModelV2> getSchedules(@RequestParam("managerName") String managerName) {
        return serviceV2.getListScheduleV2(managerName);
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class ScheduleV2DTO {
        @NotBlank
        private String manager;
    }
}
