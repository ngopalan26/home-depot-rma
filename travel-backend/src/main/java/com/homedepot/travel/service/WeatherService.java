package com.homedepot.travel.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homedepot.travel.model.Location;
import com.homedepot.travel.model.WeatherData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class WeatherService {
    
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    
    @Value("${weather.api.base-url}")
    private String weatherApiBaseUrl;
    
    @Value("${weather.api.api-key}")
    private String weatherApiKey;
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    public WeatherService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }
    
    public WeatherData getWeatherForLocation(Location location, LocalDate date) {
        try {
            String url = buildWeatherUrl(location, date);
            logger.info("Fetching weather for {} on {} from URL: {}", location.getFullName(), date, url);
            
            String response = restTemplate.getForObject(url, String.class);
            return parseWeatherResponse(response, location);
            
        } catch (RestClientException e) {
            logger.error("Error fetching weather data for {} on {}: {}", 
                        location.getFullName(), date, e.getMessage());
            return createFallbackWeatherData(location);
        } catch (Exception e) {
            logger.error("Unexpected error fetching weather data for {} on {}: {}", 
                        location.getFullName(), date, e.getMessage());
            return createFallbackWeatherData(location);
        }
    }
    
    private String buildWeatherUrl(Location location, LocalDate date) {
        String locationParam = buildLocationParameter(location);
        String dateParam = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        return String.format("%s/weather?q=%s&appid=%s&units=metric&date=%s", 
                           weatherApiBaseUrl, locationParam, weatherApiKey, dateParam);
    }
    
    private String buildLocationParameter(Location location) {
        if (location.getCountry() != null && !location.getCountry().isEmpty()) {
            return location.getCity() + "," + location.getCountry();
        }
        return location.getCity();
    }
    
    private WeatherData parseWeatherResponse(String response, Location location) throws Exception {
        JsonNode root = objectMapper.readTree(response);
        
        // Parse main weather data
        JsonNode main = root.get("main");
        JsonNode weather = root.get("weather").get(0);
        JsonNode wind = root.get("wind");
        
        Double temperature = main.get("temp").asDouble();
        Integer humidity = main.get("humidity").asInt();
        Double feelsLike = main.get("feels_like").asDouble();
        
        Double windSpeed = wind.has("speed") ? wind.get("speed").asDouble() * 3.6 : null; // Convert m/s to km/h
        Double precipitation = root.has("rain") ? root.get("rain").get("1h").asDouble() : 0.0;
        
        String condition = weather.get("main").asText();
        String description = weather.get("description").asText();
        
        Double visibility = root.has("visibility") ? root.get("visibility").asDouble() / 1000.0 : null; // Convert m to km
        
        WeatherData weatherData = new WeatherData(temperature, condition, description);
        weatherData.setHumidityPercentage(humidity);
        weatherData.setFeelsLikeCelsius(feelsLike);
        weatherData.setWindSpeedKmh(windSpeed);
        weatherData.setPrecipitationMm(precipitation);
        weatherData.setVisibilityKm(visibility);
        weatherData.setLocation(location);
        
        logger.info("Successfully parsed weather data for {}: {}°C, {}", 
                   location.getFullName(), temperature, condition);
        
        return weatherData;
    }
    
    private WeatherData createFallbackWeatherData(Location location) {
        logger.warn("Creating fallback weather data for {}", location.getFullName());
        
        // Create realistic fallback data that will generate clothing suggestions
        WeatherData fallback = new WeatherData(22.0, "Clear", "Sunny weather - demo data");
        fallback.setHumidityPercentage(60);
        fallback.setFeelsLikeCelsius(24.0);
        fallback.setWindSpeedKmh(15.0);
        fallback.setPrecipitationMm(0.0);
        fallback.setVisibilityKm(10.0);
        fallback.setUvIndex(6); // High UV for sunny suggestions
        fallback.setLocation(location);
        
        logger.info("Created fallback weather data for {}: {}°C, UV Index: {}, Humidity: {}%", 
                   location.getFullName(), 22.0, 6, 60);
        
        return fallback;
    }
    
    public boolean isApiKeyConfigured() {
        return weatherApiKey != null && 
               !weatherApiKey.isEmpty() && 
               !weatherApiKey.equals("your-api-key-here");
    }
}
