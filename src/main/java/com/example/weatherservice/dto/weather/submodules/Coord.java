package com.example.weatherservice.dto.weather.submodules;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Coord{
    private double lat;
    private double lon;
}
