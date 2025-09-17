package com.homedepot.travel.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public class TravelDayRequest {
    
    @NotNull(message = "Date is required")
    private LocalDate date;
    
    @NotNull(message = "Day number is required")
    private Integer dayNumber;
    
    @NotNull(message = "Locations are required")
    @Size(min = 1, max = 3, message = "Each day must have between 1 and 3 locations")
    @Valid
    private List<LocationRequest> locations;
    
    // Constructors
    public TravelDayRequest() {}
    
    public TravelDayRequest(LocalDate date, Integer dayNumber, List<LocationRequest> locations) {
        this.date = date;
        this.dayNumber = dayNumber;
        this.locations = locations;
    }
    
    // Getters and Setters
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
    
    public List<LocationRequest> getLocations() {
        return locations;
    }
    
    public void setLocations(List<LocationRequest> locations) {
        this.locations = locations;
    }
}
