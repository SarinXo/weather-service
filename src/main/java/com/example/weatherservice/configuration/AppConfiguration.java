package com.example.weatherservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfiguration {
    @Bean("restTemplate")
    public RestTemplate getExternalRestTemplate() {
        return new RestTemplate();
    }

}
