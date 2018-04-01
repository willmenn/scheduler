package com.broker.scheduler.controller;

import by.stub.client.StubbyClient;
import com.broker.scheduler.BrokerSchedulerApplicationTests;
import com.broker.scheduler.controller.v2.ScheduleControllerV2;
import com.broker.scheduler.service.v2.model.Plantao;
import com.broker.scheduler.service.v2.model.ScheduleModelV2;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpMethod.POST;

/**
 * Created by wahrons on 15/01/18.
 */
public class SchedulerControllerIntegrationTest extends BrokerSchedulerApplicationTests {

    @Autowired
    RestTemplate template;

    @LocalServerPort
    int randomServerPort;

    @BeforeClass
    public static void setUpClass() throws Exception {
        StubbyClient stubbyClient = new StubbyClient();
        URL resource = SchedulerControllerIntegrationTest.class.getResource("/stubby4J.yml");
        stubbyClient.startJetty(resource.getFile());
    }

    @Test
    public void shouldBeAbleToScheduleTeamOfBrokers() throws Exception {
        HttpEntity body = new HttpEntity<>(new ScheduleControllerV2.ScheduleV2DTO("MTest"));
        ScheduleModelV2 response = template.exchange("http://localhost:" + randomServerPort + "/v2/schedule",
                POST,
                body,
                new ParameterizedTypeReference<ScheduleModelV2>() {
                }).getBody();

        Map<String, Plantao> plantaoMap = response.getPlantaos().stream().collect(toMap(p -> p.getShiftPlaceId(), Function.identity()));

        Plantao plantao1 = plantaoMap.get("5917b6348d8b0061117d01ed");
        assertEquals(3, plantao1.getScheduled().get("TUE").size());
        assertEquals(2, plantao1.getScheduled().get("MON").size());

        Plantao plantao2 = plantaoMap.get("5917b6348d8b0061117d01el");
        assertEquals(1, plantao2.getScheduled().get("TUE").size());
        assertEquals(2,plantao2.getScheduled().get("SUN").size());
        assertEquals(2,plantao2.getScheduled().get("FRI").size());

        Plantao plantao3 = plantaoMap.get("5917b6348d8b0061117d01e3");
        assertEquals(2,plantao3.getScheduled().get("MON").size());
        assertEquals(0,plantao3.getScheduled().get("TUE").size());
        assertEquals(4,plantao3.getScheduled().get("WED").size());
        assertEquals(2,plantao3.getScheduled().get("FRI").size());

    }
}
