package com.example.weatherservice.controller;

import com.example.weatherservice.dto.weather.Root;
import com.example.weatherservice.dto.weather.submodules.Main;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.asn1.ocsp.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

@RestController
public class WeatherController {
    private final String appId;
    private final String weatherUrl;
    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    @Autowired
    public WeatherController(@Value("${appid}") String appId,
                             @Value("${url.external-api.weather}") String weatherUrl,
                             RestTemplate restTemplate) {
        this.appId = appId;
        this.weatherUrl = weatherUrl;
        this.restTemplate = restTemplate;
        objectMapper = new ObjectMapper();
    }
    @GetMapping(value = "/weather/lat={lat}&lon={lon}")
    @Cacheable(value = "root", key = "{#lat , #lon}")
    public ResponseEntity<Root> getWeather(@PathVariable("lat") String lat, @PathVariable("lon") String lon) {
        try{
            String request = String.format("%s?lat=%s&lon=%s&units=metric&appid=%s",
                    weatherUrl, lat, lon, appId);
            var r = restTemplate.getForObject(request, Root.class);
            return ResponseEntity.ok().body(r);
        }catch (RestClientException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //Думаю, что Weather это ближе к документу, нежели к коллекции, поэтому предоставил такое наименование
    @GetMapping("/weather/lat={lat}&lon={lon}/main")
    @Cacheable(value = "main", key = "{#lat , #lon}")
    public ResponseEntity<Main> getMain(@PathVariable("lat") String lat, @PathVariable("lon") String lon) {
        ResponseEntity<Root> root = getWeather(lat, lon);
        return root.getStatusCode() == HttpStatus.OK
                ? new ResponseEntity<>(root.getBody().getMain(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}