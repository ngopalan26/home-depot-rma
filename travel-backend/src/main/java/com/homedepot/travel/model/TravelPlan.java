package com.homedepot.travel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "travel_plans")
public class TravelPlan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @NotNull
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "traveler_name")
    private String travelerName;
    
    @OneToMany(mappedBy = "travelPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TravelDay> travelDays = new ArrayList<>();
    
    @Column(name = "created_at")
    private LocalDate createdAt;
    
    // Constructors
    public TravelPlan() {
        this.createdAt = LocalDate.now();
    }
    
    public TravelPlan(LocalDate startDate, LocalDate endDate, String travelerName) {
        this();
        this.startDate = startDate;
        this.endDate = endDate;
        this.travelerName = travelerName;
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
    
    public List<TravelDay> getTravelDays() {
        return travelDays;
    }
    
    public void setTravelDays(List<TravelDay> travelDays) {
        this.travelDays = travelDays;
    }
    
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
    
    // Helper methods
    public void addTravelDay(TravelDay travelDay) {
        travelDays.add(travelDay);
        travelDay.setTravelPlan(this);
    }
    
    public long getDurationInDays() {
        return startDate.until(endDate).getDays() + 1;
    }
}
