package com.homedepot.travel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "packing_suggestions")
public class PackingSuggestion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(name = "item_name")
    private String itemName;
    
    @Column(name = "item_category")
    private String itemCategory;
    
    @Column(name = "reasoning")
    private String reasoning;
    
    @Column(name = "priority")
    private String priority; // HIGH, MEDIUM, LOW
    
    @Column(name = "quantity")
    private Integer quantity;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_day_id")
    private TravelDay travelDay;
    
    // Constructors
    public PackingSuggestion() {}
    
    public PackingSuggestion(String itemName, String itemCategory, String reasoning, String priority) {
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.reasoning = reasoning;
        this.priority = priority;
        this.quantity = 1;
    }
    
    public PackingSuggestion(String itemName, String itemCategory, String reasoning, String priority, Integer quantity) {
        this(itemName, itemCategory, reasoning, priority);
        this.quantity = quantity;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getItemName() {
        return itemName;
    }
    
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
    public String getItemCategory() {
        return itemCategory;
    }
    
    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }
    
    public String getReasoning() {
        return reasoning;
    }
    
    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public TravelDay getTravelDay() {
        return travelDay;
    }
    
    public void setTravelDay(TravelDay travelDay) {
        this.travelDay = travelDay;
    }
    
    // Helper methods
    public boolean isHighPriority() {
        return "HIGH".equalsIgnoreCase(priority);
    }
    
    public boolean isMediumPriority() {
        return "MEDIUM".equalsIgnoreCase(priority);
    }
    
    public boolean isLowPriority() {
        return "LOW".equalsIgnoreCase(priority);
    }
}
