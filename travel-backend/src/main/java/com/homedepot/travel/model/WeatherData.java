package com.homedepot.travel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "weather_data")
public class WeatherData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "temperature_celsius")
    private Double temperatureCelsius;
    
    @Column(name = "temperature_fahrenheit")
    private Double temperatureFahrenheit;
    
    @Column(name = "humidity_percentage")
    private Integer humidityPercentage;
    
    @Column(name = "wind_speed_kmh")
    private Double windSpeedKmh;
    
    @Column(name = "precipitation_mm")
    private Double precipitationMm;
    
    @Column(name = "weather_condition")
    private String weatherCondition;
    
    @Column(name = "weather_description")
    private String weatherDescription;
    
    @Column(name = "feels_like_celsius")
    private Double feelsLikeCelsius;
    
    @Column(name = "visibility_km")
    private Double visibilityKm;
    
    @Column(name = "uv_index")
    private Integer uvIndex;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;
    
    // Constructors
    public WeatherData() {}
    
    public WeatherData(Double temperatureCelsius, String weatherCondition, String weatherDescription) {
        this.temperatureCelsius = temperatureCelsius;
        this.weatherCondition = weatherCondition;
        this.weatherDescription = weatherDescription;
        if (temperatureCelsius != null) {
            this.temperatureFahrenheit = (temperatureCelsius * 9.0 / 5.0) + 32;
        }
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Double getTemperatureCelsius() {
        return temperatureCelsius;
    }
    
    public void setTemperatureCelsius(Double temperatureCelsius) {
        this.temperatureCelsius = temperatureCelsius;
        if (temperatureCelsius != null) {
            this.temperatureFahrenheit = (temperatureCelsius * 9.0 / 5.0) + 32;
        }
    }
    
    public Double getTemperatureFahrenheit() {
        return temperatureFahrenheit;
    }
    
    public void setTemperatureFahrenheit(Double temperatureFahrenheit) {
        this.temperatureFahrenheit = temperatureFahrenheit;
    }
    
    public Integer getHumidityPercentage() {
        return humidityPercentage;
    }
    
    public void setHumidityPercentage(Integer humidityPercentage) {
        this.humidityPercentage = humidityPercentage;
    }
    
    public Double getWindSpeedKmh() {
        return windSpeedKmh;
    }
    
    public void setWindSpeedKmh(Double windSpeedKmh) {
        this.windSpeedKmh = windSpeedKmh;
    }
    
    public Double getPrecipitationMm() {
        return precipitationMm;
    }
    
    public void setPrecipitationMm(Double precipitationMm) {
        this.precipitationMm = precipitationMm;
    }
    
    public String getWeatherCondition() {
        return weatherCondition;
    }
    
    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }
    
    public String getWeatherDescription() {
        return weatherDescription;
    }
    
    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }
    
    public Double getFeelsLikeCelsius() {
        return feelsLikeCelsius;
    }
    
    public void setFeelsLikeCelsius(Double feelsLikeCelsius) {
        this.feelsLikeCelsius = feelsLikeCelsius;
    }
    
    public Double getVisibilityKm() {
        return visibilityKm;
    }
    
    public void setVisibilityKm(Double visibilityKm) {
        this.visibilityKm = visibilityKm;
    }
    
    public Integer getUvIndex() {
        return uvIndex;
    }
    
    public void setUvIndex(Integer uvIndex) {
        this.uvIndex = uvIndex;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }
    
    // Helper methods
    public boolean isRainy() {
        return weatherCondition != null && 
               (weatherCondition.toLowerCase().contains("rain") || 
                weatherCondition.toLowerCase().contains("drizzle") ||
                precipitationMm != null && precipitationMm > 0);
    }
    
    public boolean isSnowy() {
        return weatherCondition != null && 
               weatherCondition.toLowerCase().contains("snow");
    }
    
    public boolean isCold() {
        return temperatureCelsius != null && temperatureCelsius < 10;
    }
    
    public boolean isHot() {
        return temperatureCelsius != null && temperatureCelsius > 30;
    }
    
    public boolean isWindy() {
        return windSpeedKmh != null && windSpeedKmh > 20;
    }
}
