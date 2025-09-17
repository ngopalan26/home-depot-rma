package com.homedepot.travel.dto;

import java.time.LocalDate;
import java.util.List;

public class TravelDayResponse {
    
    private Long id;
    private LocalDate date;
    private Integer dayNumber;
    private List<LocationResponse> locations;
    private List<PackingSuggestionResponse> packingSuggestions;
    
    // Constructors
    public TravelDayResponse() {}
    
    public TravelDayResponse(Long id, LocalDate date, Integer dayNumber, 
                           List<LocationResponse> locations, List<PackingSuggestionResponse> packingSuggestions) {
        this.id = id;
        this.date = date;
        this.dayNumber = dayNumber;
        this.locations = locations;
        this.packingSuggestions = packingSuggestions;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public Integer getDayNumber() {
        return dayNumber;
    }
    
    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }
    
    public List<LocationResponse> getLocations() {
        return locations;
    }
    
    public void setLocations(List<LocationResponse> locations) {
        this.locations = locations;
    }
    
    public List<PackingSuggestionResponse> getPackingSuggestions() {
        return packingSuggestions;
    }
    
    public void setPackingSuggestions(List<PackingSuggestionResponse> packingSuggestions) {
        this.packingSuggestions = packingSuggestions;
    }
}
