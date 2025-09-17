package com.homedepot.travel.dto;

public class WeatherDataResponse {
    
    private Long id;
    private Double temperatureCelsius;
    private Double temperatureFahrenheit;
    private Integer humidityPercentage;
    private Double windSpeedKmh;
    private Double precipitationMm;
    private String weatherCondition;
    private String weatherDescription;
    private Double feelsLikeCelsius;
    private Double visibilityKm;
    private Integer uvIndex;
    
    // Constructors
    public WeatherDataResponse() {}
    
    public WeatherDataResponse(Long id, Double temperatureCelsius, Double temperatureFahrenheit, 
                             Integer humidityPercentage, Double windSpeedKmh, Double precipitationMm,
                             String weatherCondition, String weatherDescription, Double feelsLikeCelsius,
                             Double visibilityKm, Integer uvIndex) {
        this.id = id;
        this.temperatureCelsius = temperatureCelsius;
        this.temperatureFahrenheit = temperatureFahrenheit;
        this.humidityPercentage = humidityPercentage;
        this.windSpeedKmh = windSpeedKmh;
        this.precipitationMm = precipitationMm;
        this.weatherCondition = weatherCondition;
        this.weatherDescription = weatherDescription;
        this.feelsLikeCelsius = feelsLikeCelsius;
        this.visibilityKm = visibilityKm;
        this.uvIndex = uvIndex;
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
}
