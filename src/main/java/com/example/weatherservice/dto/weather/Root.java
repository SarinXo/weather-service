package com.example.weatherservice.dto.weather;

import com.example.weatherservice.dto.weather.submodules.Clouds;
import com.example.weatherservice.dto.weather.submodules.Coord;
import com.example.weatherservice.dto.weather.submodules.Main;
import com.example.weatherservice.dto.weather.submodules.Sys;
import com.example.weatherservice.dto.weather.submodules.Weather;
import com.example.weatherservice.dto.weather.submodules.Wind;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Root{
    private Coord coord;
    private ArrayList<Weather> weather;
    private String base;
    private Main main;
    private int visibility;
    private Wind wind;
    private Clouds clouds;
    private int dt;
    private Sys sys;
    private int timezone;
    private int id;
    private String name;
    private int cod;
}
