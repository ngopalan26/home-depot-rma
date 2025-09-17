package com.homedepot.travel.service;

import com.homedepot.travel.dto.*;
import com.homedepot.travel.model.*;
import com.homedepot.travel.repository.TravelPlanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TravelService {
    
    private static final Logger logger = LoggerFactory.getLogger(TravelService.class);
    
    @Autowired
    private WeatherService weatherService;
    
    @Autowired
    private PackingService packingService;
    
    @Autowired
    private TravelPlanRepository travelPlanRepository;
    
    public TravelPlanResponse createTravelPlan(TravelPlanRequest request) {
        logger.info("Creating travel plan for {} from {} to {}", 
                   request.getTravelerName(), request.getStartDate(), request.getEndDate());
        
        // Validate travel duration
        validateTravelPlan(request);
        
        // Create and save travel plan
        TravelPlan travelPlan = createTravelPlanEntity(request);
        travelPlan = travelPlanRepository.save(travelPlan);
        
        // Fetch weather data and generate packing suggestions
        enrichTravelPlanWithWeatherAndPacking(travelPlan);
        travelPlan = travelPlanRepository.save(travelPlan);
        
        logger.info("Successfully created travel plan with ID: {}", travelPlan.getId());
        return convertToResponse(travelPlan);
    }
    
    public TravelPlanResponse getTravelPlan(Long id) {
        logger.info("Retrieving travel plan with ID: {}", id);
        
        Optional<TravelPlan> optional = travelPlanRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException("Travel plan not found with ID: " + id);
        }
        
        TravelPlan travelPlan = optional.get();
        return convertToResponse(travelPlan);
    }
    
    private void validateTravelPlan(TravelPlanRequest request) {
        // Validate duration
        long duration = request.getDurationInDays();
        if (duration > 14) {
            throw new IllegalArgumentException("Travel duration cannot exceed 14 days");
        }
        if (duration < 1) {
            throw new IllegalArgumentException("Travel duration must be at least 1 day");
        }
        
        // Validate date consistency
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        
        // Validate travel days match duration
        if (request.getTravelDays().size() != duration) {
            throw new IllegalArgumentException("Number of travel days must match duration");
        }
        
        // Validate each travel day
        for (TravelDayRequest dayRequest : request.getTravelDays()) {
            if (dayRequest.getLocations().isEmpty()) {
                throw new IllegalArgumentException("Each travel day must have at least one location");
            }
            if (dayRequest.getLocations().size() > 3) {
                throw new IllegalArgumentException("Each travel day cannot have more than 3 locations");
            }
        }
        
        logger.info("Travel plan validation passed");
    }
    
    private TravelPlan createTravelPlanEntity(TravelPlanRequest request) {
        TravelPlan travelPlan = new TravelPlan(request.getStartDate(), request.getEndDate(), request.getTravelerName());
        
        for (TravelDayRequest dayRequest : request.getTravelDays()) {
            TravelDay travelDay = new TravelDay(dayRequest.getDate(), dayRequest.getDayNumber());
            
            for (LocationRequest locationRequest : dayRequest.getLocations()) {
                Location location = new Location(locationRequest.getCity(), locationRequest.getState(), locationRequest.getCountry(),
                                               locationRequest.getLatitude(), locationRequest.getLongitude());
                travelDay.addLocation(location);
            }
            
            travelPlan.addTravelDay(travelDay);
        }
        
        return travelPlan;
    }
    
    private void enrichTravelPlanWithWeatherAndPacking(TravelPlan travelPlan) {
        logger.info("Enriching travel plan with weather data and packing suggestions");
        
        for (TravelDay travelDay : travelPlan.getTravelDays()) {
            // Fetch weather data for each location
            for (Location location : travelDay.getLocations()) {
                try {
                    WeatherData weatherData = weatherService.getWeatherForLocation(location, travelDay.getDate());
                    location.setWeatherData(weatherData);
                    logger.info("Fetched weather data for {} on {}", location.getFullName(), travelDay.getDate());
                } catch (Exception e) {
                    logger.error("Failed to fetch weather data for {} on {}: {}", 
                               location.getFullName(), travelDay.getDate(), e.getMessage());
                    // Continue with other locations even if one fails
                }
            }
            
            // Generate packing suggestions for the day
            try {
                List<PackingSuggestion> suggestions = packingService.generatePackingSuggestions(travelDay);
                for (PackingSuggestion suggestion : suggestions) {
                    travelDay.addPackingSuggestion(suggestion);
                }
                logger.info("Generated {} packing suggestions for day {}", suggestions.size(), travelDay.getDayNumber());
            } catch (Exception e) {
                logger.error("Failed to generate packing suggestions for day {}: {}", 
                           travelDay.getDayNumber(), e.getMessage());
                // Continue even if packing suggestions fail
            }
        }
    }
    
    private TravelPlanResponse convertToResponse(TravelPlan travelPlan) {
        List<TravelDayResponse> dayResponses = new ArrayList<>();
        
        for (TravelDay travelDay : travelPlan.getTravelDays()) {
            List<LocationResponse> locationResponses = new ArrayList<>();
            
            for (Location location : travelDay.getLocations()) {
                WeatherDataResponse weatherResponse = null;
                if (location.getWeatherData() != null) {
                    WeatherData weather = location.getWeatherData();
                    weatherResponse = new WeatherDataResponse(
                        weather.getId(),
                        weather.getTemperatureCelsius(),
                        weather.getTemperatureFahrenheit(),
                        weather.getHumidityPercentage(),
                        weather.getWindSpeedKmh(),
                        weather.getPrecipitationMm(),
                        weather.getWeatherCondition(),
                        weather.getWeatherDescription(),
                        weather.getFeelsLikeCelsius(),
                        weather.getVisibilityKm(),
                        weather.getUvIndex()
                    );
                }
                
                LocationResponse locationResponse = new LocationResponse(
                    location.getId(),
                    location.getCity(),
                    location.getState(),
                    location.getCountry(),
                    location.getLatitude(),
                    location.getLongitude(),
                    weatherResponse
                );
                locationResponses.add(locationResponse);
            }
            
            List<PackingSuggestionResponse> suggestionResponses = new ArrayList<>();
            for (PackingSuggestion suggestion : travelDay.getPackingSuggestions()) {
                PackingSuggestionResponse suggestionResponse = new PackingSuggestionResponse(
                    suggestion.getId(),
                    suggestion.getItemName(),
                    suggestion.getItemCategory(),
                    suggestion.getReasoning(),
                    suggestion.getPriority(),
                    suggestion.getQuantity()
                );
                suggestionResponses.add(suggestionResponse);
            }
            
            TravelDayResponse dayResponse = new TravelDayResponse(
                travelDay.getId(),
                travelDay.getDate(),
                travelDay.getDayNumber(),
                locationResponses,
                suggestionResponses
            );
            dayResponses.add(dayResponse);
        }
        
        return new TravelPlanResponse(
            travelPlan.getId(),
            travelPlan.getStartDate(),
            travelPlan.getEndDate(),
            travelPlan.getTravelerName(),
            dayResponses,
            travelPlan.getCreatedAt()
        );
    }
}
