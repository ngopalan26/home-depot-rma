package com.homedepot.travel.service;

import com.homedepot.travel.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PackingService {
    
    private static final Logger logger = LoggerFactory.getLogger(PackingService.class);
    
    public List<PackingSuggestion> generatePackingSuggestions(TravelDay travelDay) {
        List<PackingSuggestion> suggestions = new ArrayList<>();
        
        logger.info("Generating packing suggestions for day {} ({})", 
                   travelDay.getDayNumber(), travelDay.getDate());
        
        // Add basic travel essentials
        suggestions.addAll(getBasicEssentials());
        
        // Generate weather-based suggestions for each location
        for (Location location : travelDay.getLocations()) {
            WeatherData weather = location.getWeatherData();
            if (weather != null) {
                logger.info("Generating suggestions for location: {} with weather: {}°C, condition: {}", 
                           location.getFullName(), weather.getTemperatureCelsius(), weather.getWeatherCondition());
                List<PackingSuggestion> weatherSuggestions = generateWeatherBasedSuggestions(weather);
                logger.info("Generated {} weather-based suggestions for {}", weatherSuggestions.size(), location.getFullName());
                suggestions.addAll(weatherSuggestions);
            } else {
                logger.warn("No weather data available for location: {}", location.getFullName());
            }
        }
        
        // Remove duplicates and consolidate suggestions
        List<PackingSuggestion> consolidated = consolidateSuggestions(suggestions);
        logger.info("Final consolidated suggestions for day {}: {} items", travelDay.getDayNumber(), consolidated.size());
        return consolidated;
    }
    
    private List<PackingSuggestion> getBasicEssentials() {
        // Only clothing essentials - no documents, electronics, or toiletries
        return new ArrayList<>();
    }
    
    private List<PackingSuggestion> generateWeatherBasedSuggestions(WeatherData weather) {
        List<PackingSuggestion> suggestions = new ArrayList<>();
        
        // Get weather condition for detailed analysis
        String condition = weather.getWeatherCondition() != null ? 
                          weather.getWeatherCondition().toLowerCase() : "";
        boolean isSunny = condition.contains("sun") || condition.contains("clear") || condition.contains("fair");
        boolean isRainy = condition.contains("rain") || condition.contains("drizzle") || condition.contains("shower");
        boolean isSnowy = condition.contains("snow") || condition.contains("blizzard") || condition.contains("sleet");
        boolean isCloudy = condition.contains("cloud") || condition.contains("overcast");
        
        // Temperature analysis
        Double tempF = weather.getTemperatureFahrenheit();
        Integer humidity = weather.getHumidityPercentage();
        Integer uvIndex = weather.getUvIndex();
        
        // SUNNY WEATHER
        if (isSunny || (tempF != null && tempF > 75)) {
            suggestions.addAll(getSunnyWeatherClothing(tempF, uvIndex));
        }
        
        // HOT WEATHER
        if (tempF != null && tempF > 85) {
            suggestions.addAll(getHotWeatherClothing(tempF, humidity));
        }
        
        // COLD WEATHER
        if (tempF != null && tempF < 50) {
            suggestions.addAll(getColdWeatherClothing(tempF, condition));
        }
        
        // RAINY WEATHER
        if (isRainy || (weather.getPrecipitationMm() != null && weather.getPrecipitationMm() > 0)) {
            suggestions.addAll(getRainyWeatherClothing(tempF, humidity));
        }
        
        // HUMID WEATHER
        if (humidity != null && humidity > 70) {
            suggestions.addAll(getHumidWeatherClothing(tempF, humidity));
        }
        
        // SNOWY WEATHER
        if (isSnowy || (tempF != null && tempF < 32)) {
            suggestions.addAll(getSnowyWeatherClothing(tempF));
        }
        
        // WINDY CONDITIONS
        if (weather.getWindSpeedKmh() != null && weather.getWindSpeedKmh() > 20) {
            suggestions.add(new PackingSuggestion("Windbreaker or Wind-Resistant Jacket", "Clothing", 
                                               "Strong winds expected (" + weather.getWindSpeedKmh() + " km/h) - prevents wind chill", "MEDIUM", 1));
        }
        
        return suggestions;
    }
    
    private List<PackingSuggestion> getSunnyWeatherClothing(Double tempF, Integer uvIndex) {
        List<PackingSuggestion> suggestions = new ArrayList<>();
        
        suggestions.add(new PackingSuggestion("Light Cotton T-Shirts", "Clothing", 
                                           "Sunny weather - choose light colors to reflect heat", "HIGH", 3));
        suggestions.add(new PackingSuggestion("Linen or Cotton Shorts", "Clothing", 
                                           "Sunny weather - breathable and comfortable", "HIGH", 2));
        suggestions.add(new PackingSuggestion("Lightweight Cotton Pants", "Clothing", 
                                           "Sunny weather - for sun protection and comfort", "MEDIUM", 2));
        
        if (uvIndex != null && uvIndex > 5) {
            suggestions.add(new PackingSuggestion("Long-Sleeved UV Protection Shirt", "Clothing", 
                                               "High UV index (" + uvIndex + ") - UPF 50+ protection", "HIGH", 2));
            suggestions.add(new PackingSuggestion("Wide-Brimmed Sun Hat", "Accessories", 
                                               "High UV index (" + uvIndex + ") - protects face, neck, and ears", "HIGH", 1));
            suggestions.add(new PackingSuggestion("Lightweight Long Pants", "Clothing", 
                                               "High UV index (" + uvIndex + ") - protects legs from sun", "MEDIUM", 1));
        }
        
        if (tempF != null && tempF > 80) {
            suggestions.add(new PackingSuggestion("Breathable Tank Tops", "Clothing", 
                                               "Hot sunny weather - maximum airflow", "MEDIUM", 2));
        }
        
        return suggestions;
    }
    
    private List<PackingSuggestion> getHotWeatherClothing(Double tempF, Integer humidity) {
        List<PackingSuggestion> suggestions = new ArrayList<>();
        
        suggestions.add(new PackingSuggestion("Moisture-Wicking Athletic Shirts", "Clothing", 
                                           "Hot weather - wicks sweat away from body", "HIGH", 3));
        suggestions.add(new PackingSuggestion("Lightweight Shorts", "Clothing", 
                                           "Hot weather - allows air circulation", "HIGH", 2));
        suggestions.add(new PackingSuggestion("Loose-Fitting Cotton Shirts", "Clothing", 
                                           "Hot weather - loose fit allows air flow", "MEDIUM", 2));
        
        if (humidity != null && humidity > 60) {
            suggestions.add(new PackingSuggestion("Quick-Dry Performance Shorts", "Clothing", 
                                               "Hot and humid - dries quickly after sweating", "HIGH", 2));
            suggestions.add(new PackingSuggestion("Breathable Mesh Fabric Shirts", "Clothing", 
                                               "Hot and humid - maximum ventilation", "MEDIUM", 2));
        }
        
        if (tempF != null && tempF > 90) {
            suggestions.add(new PackingSuggestion("Ultra-Lightweight Clothing", "Clothing", 
                                               "Extreme heat - minimal fabric weight", "HIGH", 2));
            suggestions.add(new PackingSuggestion("Ventilated Hiking Shorts", "Clothing", 
                                               "Extreme heat - built-in ventilation panels", "MEDIUM", 1));
        }
        
        return suggestions;
    }
    
    private List<PackingSuggestion> getColdWeatherClothing(Double tempF, String condition) {
        List<PackingSuggestion> suggestions = new ArrayList<>();
        
        if (tempF != null && tempF < 32) {
            // Below freezing
            suggestions.add(new PackingSuggestion("Insulated Winter Parka", "Clothing", 
                                               "Below freezing (" + tempF + "°F) - essential for warmth", "HIGH", 1));
            suggestions.add(new PackingSuggestion("Thermal Base Layers", "Clothing", 
                                               "Below freezing - wicks moisture, retains heat", "HIGH", 2));
            suggestions.add(new PackingSuggestion("Fleece Lined Sweaters", "Clothing", 
                                               "Below freezing - excellent insulation", "HIGH", 2));
            suggestions.add(new PackingSuggestion("Insulated Gloves", "Accessories", 
                                               "Below freezing - prevents frostbite", "HIGH", 1));
            suggestions.add(new PackingSuggestion("Wool Beanie or Balaclava", "Accessories", 
                                               "Below freezing - protects head and face", "HIGH", 1));
        } else {
            // Above freezing but cold
            suggestions.add(new PackingSuggestion("Medium Weight Jacket", "Clothing", 
                                               "Cold weather (" + tempF + "°F) - wind resistant", "HIGH", 1));
            suggestions.add(new PackingSuggestion("Long Sleeve Thermal Shirts", "Clothing", 
                                               "Cold weather - base layer for warmth", "HIGH", 2));
            suggestions.add(new PackingSuggestion("Fleece Sweaters", "Clothing", 
                                               "Cold weather - lightweight insulation", "MEDIUM", 2));
            suggestions.add(new PackingSuggestion("Warm Long Pants", "Clothing", 
                                               "Cold weather - protects legs from cold", "HIGH", 2));
        }
        
        if (condition.contains("wind")) {
            suggestions.add(new PackingSuggestion("Wind-Resistant Outer Layer", "Clothing", 
                                               "Cold and windy - prevents wind chill", "MEDIUM", 1));
        }
        
        return suggestions;
    }
    
    private List<PackingSuggestion> getRainyWeatherClothing(Double tempF, Integer humidity) {
        List<PackingSuggestion> suggestions = new ArrayList<>();
        
        suggestions.add(new PackingSuggestion("Waterproof Rain Jacket", "Clothing", 
                                           "Rainy weather - essential protection", "HIGH", 1));
        suggestions.add(new PackingSuggestion("Waterproof Pants", "Clothing", 
                                           "Rainy weather - keeps legs dry", "MEDIUM", 1));
        
        if (tempF != null && tempF < 60) {
            suggestions.add(new PackingSuggestion("Insulated Rain Jacket", "Clothing", 
                                               "Cold and rainy - provides warmth and dryness", "HIGH", 1));
            suggestions.add(new PackingSuggestion("Waterproof Insulated Boots", "Footwear", 
                                               "Cold and rainy - warm and dry feet", "HIGH", 1));
        } else {
            suggestions.add(new PackingSuggestion("Lightweight Rain Jacket", "Clothing", 
                                               "Warm and rainy - breathable waterproof", "HIGH", 1));
            suggestions.add(new PackingSuggestion("Quick-Dry Shorts", "Clothing", 
                                               "Warm and rainy - dries quickly if wet", "MEDIUM", 1));
        }
        
        suggestions.add(new PackingSuggestion("Moisture-Wicking Base Layers", "Clothing", 
                                           "Rainy weather - stays dry against skin", "MEDIUM", 2));
        
        return suggestions;
    }
    
    private List<PackingSuggestion> getHumidWeatherClothing(Double tempF, Integer humidity) {
        List<PackingSuggestion> suggestions = new ArrayList<>();
        
        suggestions.add(new PackingSuggestion("Moisture-Wicking Performance Shirts", "Clothing", 
                                           "High humidity (" + humidity + "%) - pulls sweat away", "HIGH", 3));
        suggestions.add(new PackingSuggestion("Breathable Mesh Shorts", "Clothing", 
                                           "High humidity - maximum air circulation", "HIGH", 2));
        suggestions.add(new PackingSuggestion("Quick-Dry Underwear", "Clothing", 
                                           "High humidity - prevents chafing and discomfort", "HIGH", 3));
        
        if (tempF != null && tempF > 80) {
            suggestions.add(new PackingSuggestion("Loose-Fitting Cotton Shirts", "Clothing", 
                                               "Hot and humid - allows air flow", "MEDIUM", 2));
            suggestions.add(new PackingSuggestion("Ventilated Athletic Shorts", "Clothing", 
                                               "Hot and humid - built-in ventilation", "MEDIUM", 2));
        }
        
        suggestions.add(new PackingSuggestion("Anti-Microbial Socks", "Accessories", 
                                           "High humidity - prevents foot odor and moisture", "MEDIUM", 3));
        
        return suggestions;
    }
    
    private List<PackingSuggestion> getSnowyWeatherClothing(Double tempF) {
        List<PackingSuggestion> suggestions = new ArrayList<>();
        
        suggestions.add(new PackingSuggestion("Insulated Snow Jacket", "Clothing", 
                                           "Snowy weather - waterproof and warm", "HIGH", 1));
        suggestions.add(new PackingSuggestion("Waterproof Snow Pants", "Clothing", 
                                           "Snowy weather - keeps legs dry and warm", "HIGH", 1));
        suggestions.add(new PackingSuggestion("Insulated Snow Boots", "Footwear", 
                                           "Snowy weather - waterproof and insulated", "HIGH", 1));
        suggestions.add(new PackingSuggestion("Thermal Base Layers", "Clothing", 
                                           "Snowy weather - essential warmth layer", "HIGH", 2));
        suggestions.add(new PackingSuggestion("Fleece Lined Sweaters", "Clothing", 
                                           "Snowy weather - excellent insulation", "HIGH", 2));
        suggestions.add(new PackingSuggestion("Insulated Gloves", "Accessories", 
                                           "Snowy weather - prevents frostbite", "HIGH", 1));
        suggestions.add(new PackingSuggestion("Wool Beanie", "Accessories", 
                                           "Snowy weather - keeps head warm", "HIGH", 1));
        
        if (tempF != null && tempF < 20) {
            suggestions.add(new PackingSuggestion("Down Insulated Parka", "Clothing", 
                                               "Extreme cold (" + tempF + "°F) - maximum warmth", "HIGH", 1));
            suggestions.add(new PackingSuggestion("Balaclava or Face Mask", "Accessories", 
                                               "Extreme cold - protects face from frostbite", "HIGH", 1));
        }
        
        return suggestions;
    }
    
    private List<PackingSuggestion> consolidateSuggestions(List<PackingSuggestion> suggestions) {
        List<PackingSuggestion> consolidated = new ArrayList<>();
        
        // Group suggestions by item name and combine quantities
        for (PackingSuggestion suggestion : suggestions) {
            boolean found = false;
            
            for (PackingSuggestion existing : consolidated) {
                if (existing.getItemName().equals(suggestion.getItemName())) {
                    // Combine quantities and update reasoning
                    existing.setQuantity(existing.getQuantity() + suggestion.getQuantity());
                    existing.setReasoning(existing.getReasoning() + "; " + suggestion.getReasoning());
                    
                    // Keep the higher priority
                    if (suggestion.isHighPriority() && !existing.isHighPriority()) {
                        existing.setPriority("HIGH");
                    } else if (suggestion.isMediumPriority() && existing.isLowPriority()) {
                        existing.setPriority("MEDIUM");
                    }
                    
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                consolidated.add(suggestion);
            }
        }
        
        // Sort by priority (HIGH, MEDIUM, LOW)
        consolidated.sort((a, b) -> {
            int priorityOrder = getPriorityOrder(a.getPriority()) - getPriorityOrder(b.getPriority());
            if (priorityOrder == 0) {
                return a.getItemName().compareTo(b.getItemName());
            }
            return priorityOrder;
        });
        
        logger.info("Generated {} consolidated packing suggestions", consolidated.size());
        return consolidated;
    }
    
    private int getPriorityOrder(String priority) {
        switch (priority.toUpperCase()) {
            case "HIGH": return 1;
            case "MEDIUM": return 2;
            case "LOW": return 3;
            default: return 4;
        }
    }
}
