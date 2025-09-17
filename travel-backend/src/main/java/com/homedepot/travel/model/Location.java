package com.homedepot.travel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "locations")
public class Location {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(max = 100)
    @Column(name = "city")
    private String city;
    
    @Size(max = 100)
    @Column(name = "state")
    private String state;
    
    @Size(max = 100)
    @Column(name = "country")
    private String country;
    
    @Column(name = "latitude")
    private Double latitude;
    
    @Column(name = "longitude")
    private Double longitude;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_day_id")
    private TravelDay travelDay;
    
    @OneToOne(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private WeatherData weatherData;
    
    // Constructors
    public Location() {}
    
    public Location(String city, String state, String country) {
        this.city = city;
        this.state = state;
        this.country = country;
    }
    
    public Location(String city, String state, String country, Double latitude, Double longitude) {
        this(city, state, country);
        this.latitude = latitude;
        this.longitude = longitude;
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
    
    public TravelDay getTravelDay() {
        return travelDay;
    }
    
    public void setTravelDay(TravelDay travelDay) {
        this.travelDay = travelDay;
    }
    
    public WeatherData getWeatherData() {
        return weatherData;
    }
    
    public void setWeatherData(WeatherData weatherData) {
        this.weatherData = weatherData;
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
