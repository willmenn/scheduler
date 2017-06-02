package com.broker.scheduler.client;

import com.broker.scheduler.model.ShiftPlace;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Component
@FeignClient(url = "${brokermanagement.url}", name = "shiftPlaceClient")
public interface ShifPlaceClient {

    @RequestMapping(value = "/shiftPlace/manager/{name}", method = GET)
    List<ShiftPlace> fetchShiftPlaceByManager(@PathVariable("name") String name);
}
