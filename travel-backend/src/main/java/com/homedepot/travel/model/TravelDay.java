package com.homedepot.travel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "travel_days")
public class TravelDay {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(name = "date")
    private LocalDate date;
    
    @Column(name = "day_number")
    private Integer dayNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_plan_id")
    private TravelPlan travelPlan;
    
    @OneToMany(mappedBy = "travelDay", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Location> locations = new ArrayList<>();
    
    @OneToMany(mappedBy = "travelDay", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PackingSuggestion> packingSuggestions = new ArrayList<>();
    
    // Constructors
    public TravelDay() {}
    
    public TravelDay(LocalDate date, Integer dayNumber) {
        this.date = date;
        this.dayNumber = dayNumber;
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
    
    public TravelPlan getTravelPlan() {
        return travelPlan;
    }
    
    public void setTravelPlan(TravelPlan travelPlan) {
        this.travelPlan = travelPlan;
    }
    
    public List<Location> getLocations() {
        return locations;
    }
    
    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
    
    public List<PackingSuggestion> getPackingSuggestions() {
        return packingSuggestions;
    }
    
    public void setPackingSuggestions(List<PackingSuggestion> packingSuggestions) {
        this.packingSuggestions = packingSuggestions;
    }
    
    // Helper methods
    public void addLocation(Location location) {
        locations.add(location);
        location.setTravelDay(this);
    }
    
    public void addPackingSuggestion(PackingSuggestion suggestion) {
        packingSuggestions.add(suggestion);
        suggestion.setTravelDay(this);
    }
}
