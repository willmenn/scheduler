package com.broker.scheduler.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by wahrons on 15/01/18.
 */
@Configuration
public class IntegrationTestConfiguration {

    @Bean
    public RestTemplate buildRestTemplateForIntegrationTest(){
        HttpClient client = HttpClientBuilder
                .create()
                .useSystemProperties()
                .build();

        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(client));
    }
}
