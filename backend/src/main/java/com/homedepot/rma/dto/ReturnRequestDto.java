package com.homedepot.rma.dto;

import com.homedepot.rma.enums.ReturnMethod;
import com.homedepot.rma.enums.ReturnReason;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ReturnRequestDto {
    
    @NotBlank
    private String orderNumber;
    
    @NotNull
    private ReturnReason reason;
    
    @NotNull
    private ReturnMethod method;
    
    private String notes;
    
    @NotNull
    private List<ReturnItemDto> returnItems;
    
    // Constructors
    public ReturnRequestDto() {}
    
    public ReturnRequestDto(String orderNumber, ReturnReason reason, ReturnMethod method, List<ReturnItemDto> returnItems) {
        this.orderNumber = orderNumber;
        this.reason = reason;
        this.method = method;
        this.returnItems = returnItems;
    }
    
    // Getters and Setters
    public String getOrderNumber() {
        return orderNumber;
    }
    
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    
    public ReturnReason getReason() {
        return reason;
    }
    
    public void setReason(ReturnReason reason) {
        this.reason = reason;
    }
    
    public ReturnMethod getMethod() {
        return method;
    }
    
    public void setMethod(ReturnMethod method) {
        this.method = method;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public List<ReturnItemDto> getReturnItems() {
        return returnItems;
    }
    
    public void setReturnItems(List<ReturnItemDto> returnItems) {
        this.returnItems = returnItems;
    }
}
