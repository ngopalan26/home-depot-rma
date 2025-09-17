package com.homedepot.travel.dto;

import java.time.LocalDate;
import java.util.List;

public class TravelPlanResponse {
    
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String travelerName;
    private List<TravelDayResponse> travelDays;
    private LocalDate createdAt;
    private long durationInDays;
    
    // Constructors
    public TravelPlanResponse() {}
    
    public TravelPlanResponse(Long id, LocalDate startDate, LocalDate endDate, String travelerName, 
                             List<TravelDayResponse> travelDays, LocalDate createdAt) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.travelerName = travelerName;
        this.travelDays = travelDays;
        this.createdAt = createdAt;
        this.durationInDays = startDate.until(endDate).getDays() + 1;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public List<TravelDayResponse> getTravelDays() {
        return travelDays;
    }
    
    public void setTravelDays(List<TravelDayResponse> travelDays) {
        this.travelDays = travelDays;
    }
    
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
    
    public long getDurationInDays() {
        return durationInDays;
    }
    
    public void setDurationInDays(long durationInDays) {
        this.durationInDays = durationInDays;
    }
}
