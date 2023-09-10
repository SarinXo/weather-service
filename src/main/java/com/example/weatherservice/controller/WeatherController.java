package com.example.weatherservice.controller;

import com.example.weatherservice.dto.weather.Root;
import com.example.weatherservice.dto.weather.submodules.Coord;
import com.example.weatherservice.dto.weather.submodules.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@RestController
public class WeatherController {
    private final String appId;
    private final String weatherUrl;
    private final RestTemplate restTemplate;

    @Autowired
    public WeatherController(@Value("${appid}") String appId,
                             @Value("${url.weather}") String weatherUrl,
                             RestTemplate restTemplate) {
        this.appId = appId;
        this.weatherUrl = weatherUrl;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/weather")
    @Cacheable(value = "root", key = "#coord")
    public Root getWeather(@RequestBody Coord coord) throws InterruptedException {//54.1838   45.1749 - Saransk
        TimeUnit.SECONDS.sleep(5);
        return restTemplate.getForObject("https://api.openweathermap.org/data/2.5/weather?" +
                    "lat=" + coord.getLat() +
                    "&lon=" + coord.getLon() +
                            "&units=metric&appid=" + appId, Root.class);
    }

    @GetMapping("/weathers")
    public Main getWeather(@RequestParam String lat, @RequestParam String lon) {
        //Есть ли способ сделать красивее?
        String request = weatherUrl +
                "?lat=" + lat +
                "&lon=" + lon +
                "&units=metric&appid=" + appId;
        // Main не может быть null по определению
        return restTemplate.getForObject(request, Root.class).getMain();
    }




}