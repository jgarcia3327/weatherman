package com.example.rest_service.controllers;

import com.example.rest_service.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/v1/weather")
    public ResponseEntity<Map<String, Object>> weather(@RequestParam(value="city", defaultValue = "Melbourne") String city) {
        Map<String, Object> result = weatherService.fetchWeather(city);
        return Objects.isNull(result)? new ResponseEntity<>(null, HttpStatus.BAD_REQUEST) : new ResponseEntity<>(result, HttpStatus.OK);
    }
}
