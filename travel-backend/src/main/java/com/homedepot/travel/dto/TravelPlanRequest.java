package com.homedepot.travel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public class TravelPlanRequest {
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    
    @NotBlank(message = "Traveler name is required")
    @Size(min = 1, max = 50, message = "Traveler name must be between 1 and 50 characters")
    private String travelerName;
    
    @NotNull(message = "Travel days are required")
    @Size(min = 1, max = 14, message = "Travel plan must have between 1 and 14 days")
    private List<TravelDayRequest> travelDays;
    
    // Constructors
    public TravelPlanRequest() {}
    
    public TravelPlanRequest(LocalDate startDate, LocalDate endDate, String travelerName, List<TravelDayRequest> travelDays) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.travelerName = travelerName;
        this.travelDays = travelDays;
    }
    
    // Getters and Setters
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public String getTravelerName() {
        return travelerName;
    }
    
    public void setTravelerName(String travelerName) {
        this.travelerName = travelerName;
    }
    
    public List<TravelDayRequest> getTravelDays() {
        return travelDays;
    }
    
    public void setTravelDays(List<TravelDayRequest> travelDays) {
        this.travelDays = travelDays;
    }
    
    // Helper methods
    public long getDurationInDays() {
        return startDate.until(endDate).getDays() + 1;
    }
}
