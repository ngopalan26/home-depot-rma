package com.homedepot.rma.model;

import com.homedepot.rma.enums.ReturnItemStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "return_items")
public class ReturnItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_request_id")
    private ReturnRequest returnRequest;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;
    
    @NotNull
    @Positive
    private Integer quantityToReturn;
    
    private String condition;
    private String notes;
    
    @Enumerated(EnumType.STRING)
    private ReturnItemStatus status;
    
    // Constructors
    public ReturnItem() {
        this.status = ReturnItemStatus.PENDING;
    }
    
    public ReturnItem(ReturnRequest returnRequest, OrderItem orderItem, Integer quantityToReturn) {
        this();
        this.returnRequest = returnRequest;
        this.orderItem = orderItem;
        this.quantityToReturn = quantityToReturn;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public ReturnRequest getReturnRequest() {
        return returnRequest;
    }
    
    public void setReturnRequest(ReturnRequest returnRequest) {
        this.returnRequest = returnRequest;
    }
    
    public OrderItem getOrderItem() {
        return orderItem;
    }
    
    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
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
    
    public ReturnItemStatus getStatus() {
        return status;
    }
    
    public void setStatus(ReturnItemStatus status) {
        this.status = status;
    }
}
