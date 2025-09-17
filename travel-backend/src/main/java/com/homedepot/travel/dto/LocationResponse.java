package com.homedepot.travel.dto;

public class LocationResponse {
    
    private Long id;
    private String city;
    private String state;
    private String country;
    private Double latitude;
    private Double longitude;
    private String fullName;
    private WeatherDataResponse weatherData;
    
    // Constructors
    public LocationResponse() {}
    
    public LocationResponse(Long id, String city, String state, String country, Double latitude, Double longitude, WeatherDataResponse weatherData) {
        this.id = id;
        this.city = city;
        this.state = state;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.weatherData = weatherData;
        this.fullName = buildFullName();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
        this.fullName = buildFullName();
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
        this.fullName = buildFullName();
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
        this.fullName = buildFullName();
    }
    
    public Double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    
    public Double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public WeatherDataResponse getWeatherData() {
        return weatherData;
    }
    
    public void setWeatherData(WeatherDataResponse weatherData) {
        this.weatherData = weatherData;
    }
    
    // Helper methods
    private String buildFullName() {
        StringBuilder fullName = new StringBuilder(city);
        
        if (state != null && !state.isEmpty()) {
            fullName.append(", ").append(state);
        }
        
        if (country != null && !country.isEmpty()) {
            fullName.append(", ").append(country);
        }
        
        return fullName.toString();
    }
}
