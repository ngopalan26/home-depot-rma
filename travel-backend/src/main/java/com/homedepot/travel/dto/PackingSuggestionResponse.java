package com.homedepot.travel.dto;

public class PackingSuggestionResponse {
    
    private Long id;
    private String itemName;
    private String itemCategory;
    private String reasoning;
    private String priority;
    private Integer quantity;
    
    // Constructors
    public PackingSuggestionResponse() {}
    
    public PackingSuggestionResponse(Long id, String itemName, String itemCategory, String reasoning, String priority, Integer quantity) {
        this.id = id;
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.reasoning = reasoning;
        this.priority = priority;
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
}
