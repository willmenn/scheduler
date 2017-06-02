package com.broker.scheduler.client;


import com.broker.scheduler.model.Broker;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Component
@FeignClient(url = "${brokermanagement.url}", name = "managerClient")
public interface BrokerClient {

    @RequestMapping(value = "/brokers/manager/{name}", method = GET)
    List<Broker> fetchBrokersByManager(@PathVariable("name") String name);
}
