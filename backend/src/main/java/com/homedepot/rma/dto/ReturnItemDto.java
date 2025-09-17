package com.homedepot.rma.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ReturnItemDto {
    
    @NotNull
    private Long orderItemId;
    
    @NotNull
    @Positive
    private Integer quantityToReturn;
    
    private String condition;
    private String notes;
    
    // Constructors
    public ReturnItemDto() {}
    
    public ReturnItemDto(Long orderItemId, Integer quantityToReturn) {
        this.orderItemId = orderItemId;
        this.quantityToReturn = quantityToReturn;
    }
    
    // Getters and Setters
    public Long getOrderItemId() {
        return orderItemId;
    }
    
    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }
    
    public Integer getQuantityToReturn() {
        return quantityToReturn;
    }
    
    public void setQuantityToReturn(Integer quantityToReturn) {
        this.quantityToReturn = quantityToReturn;
    }
    
    public String getCondition() {
        return condition;
    }
    
    public void setCondition(String condition) {
        this.condition = condition;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
