package com.example.rest_service.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    public Map<String, Object> fetchWeather(String city) {

        try {
            // Call WeatherStack
            return this.fetchWeatherStack(city);
        } catch (Exception e) {
            // Fail over
            try {
                // Call OpenWeatherMap
                return this.fetchOpenWeatherMap(city);
            } catch (Exception fe) {
                //Stale FIXME
                return null;
            }
        }
    }

    private HashMap<String, Object> fetchWeatherStack(String city) {

        final String uri = "https://api.weatherstack.com/current?access_key=d5c9d920c27f4ba0876dab5d80361272&units=m&query=" + city;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        final JsonObject resultJsonObject = JsonParser.parseString(result).getAsJsonObject();
        final Float windSpeed = resultJsonObject.get("current").getAsJsonObject().get("wind_speed").getAsFloat();
        final Float temperature = resultJsonObject.get("current").getAsJsonObject().get("temperature").getAsFloat();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("wind_speed", windSpeed);
        map.put("temperature_degrees", temperature);

        return map;
    }

    private HashMap<String, Object> fetchOpenWeatherMap(String city) {

        StringBuilder sbCity = new StringBuilder(city);
        if (city.equalsIgnoreCase("melbourne")) sbCity.append(",AU"); // Prioritize AU when city = Melbourne
        final String uri = "https://api.openweathermap.org/data/2.5/weather?appid=2326504fb9b100bee21400190e4dbe6d&units=metric&q=" + sbCity;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        final JsonObject resultJsonObject = JsonParser.parseString(result).getAsJsonObject();
        final Float windSpeed = resultJsonObject.get("wind").getAsJsonObject().get("speed").getAsFloat();
        final Float temperature = resultJsonObject.get("main").getAsJsonObject().get("temp").getAsFloat();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("wind_speed", windSpeed);
        map.put("temperature_degrees", temperature);

        return map;
    }

}
