package com.homedepot.travel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LocationRequest {
    
    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City name must not exceed 100 characters")
    private String city;
    
    @Size(max = 100, message = "State name must not exceed 100 characters")
    private String state;
    
    @Size(max = 100, message = "Country name must not exceed 100 characters")
    private String country;
    
    private Double latitude;
    
    private Double longitude;
    
    // Constructors
    public LocationRequest() {}
    
    public LocationRequest(String city, String state, String country) {
        this.city = city;
        this.state = state;
        this.country = country;
    }
    
    public LocationRequest(String city, String state, String country, Double latitude, Double longitude) {
        this(city, state, country);
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    // Getters and Setters
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
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
    
    // Helper methods
    public String getFullName() {
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
