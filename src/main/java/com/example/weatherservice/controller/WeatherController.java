package com.example.weatherservice.controller;

import com.example.weatherservice.dto.weather.Root;
import com.example.weatherservice.dto.weather.submodules.Coord;
import com.example.weatherservice.dto.weather.submodules.Main;
import org.bouncycastle.oer.its.etsi102941.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
public class WeatherController {
    private final String appId;
    private final String weatherUrl;
    private final RestTemplate externalRestTemplate;

    @Autowired
    public WeatherController(@Value("${appid}") String appId,
                             @Value("${url.external-api.weather}") String weatherUrl,
                             RestTemplate externalRestTemplate) {
        this.appId = appId;
        this.weatherUrl = weatherUrl;
        this.externalRestTemplate = externalRestTemplate;
    }

    @GetMapping("/weather/lat={lat}&lon={lon}")
    @Cacheable(value = "root", key = "{#lat , #lon}")
    public ResponseEntity<Root> getWeather(@PathVariable("lat") String lat, @PathVariable("lon") String lon) {
        //54.1838 45.1749 - Saransk
        // Задержка для первого вызова, дальше мы будем подменять этот метод прокси и брать значение из кеша
        /*try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/

        try{
            String request = String.format("%s?lat=%s&lon=%s&units=metric&appid=%s",
                    weatherUrl, lat, lon, appId);
            return externalRestTemplate.getForEntity(request, Root.class);
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