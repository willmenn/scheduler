package com.broker.scheduler.client;

import com.broker.scheduler.model.ShiftPlace;
import com.broker.scheduler.service.v2.model.Plantao;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Component
@FeignClient(url = "${brokermanagement.url}", name = "shiftPlaceClient")
public interface ShiftPlaceClient {

    @RequestMapping(value = "/shiftPlace/manager/{name}", method = GET)
    List<ShiftPlace> fetchShiftPlaceByManager(@PathVariable("name") String name);

    @RequestMapping(value = "/shiftPlace/manager/{name}", method = GET)
    List<Plantao> fetchShiftPlaceByManagerV2(@PathVariable("name") String name);
}
