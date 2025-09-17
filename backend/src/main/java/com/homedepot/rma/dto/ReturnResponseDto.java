package com.homedepot.rma.dto;

import com.homedepot.rma.enums.ReturnMethod;
import com.homedepot.rma.enums.ReturnReason;
import com.homedepot.rma.enums.ReturnStatus;

import java.time.LocalDateTime;

public class ReturnResponseDto {
    
    private String rmaNumber;
    private String orderNumber;
    private ReturnReason reason;
    private ReturnMethod method;
    private ReturnStatus status;
    private String notes;
    private String trackingNumber;
    private String qrCodeData;
    private String shippingLabelUrl;
    private LocalDateTime requestedDate;
    private LocalDateTime processedDate;
    private LocalDateTime completedDate;
    
    // Constructors
    public ReturnResponseDto() {}
    
    public ReturnResponseDto(String rmaNumber, String orderNumber, ReturnReason reason, 
                           ReturnMethod method, ReturnStatus status) {
        this.rmaNumber = rmaNumber;
        this.orderNumber = orderNumber;
        this.reason = reason;
        this.method = method;
        this.status = status;
        this.requestedDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getRmaNumber() {
        return rmaNumber;
    }
    
    public void setRmaNumber(String rmaNumber) {
        this.rmaNumber = rmaNumber;
    }
    
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
    
    public ReturnStatus getStatus() {
        return status;
    }
    
    public void setStatus(ReturnStatus status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getTrackingNumber() {
        return trackingNumber;
    }
    
    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }
    
    public String getQrCodeData() {
        return qrCodeData;
    }
    
    public void setQrCodeData(String qrCodeData) {
        this.qrCodeData = qrCodeData;
    }
    
    public String getShippingLabelUrl() {
        return shippingLabelUrl;
    }
    
    public void setShippingLabelUrl(String shippingLabelUrl) {
        this.shippingLabelUrl = shippingLabelUrl;
    }
    
    public LocalDateTime getRequestedDate() {
        return requestedDate;
    }
    
    public void setRequestedDate(LocalDateTime requestedDate) {
        this.requestedDate = requestedDate;
    }
    
    public LocalDateTime getProcessedDate() {
        return processedDate;
    }
    
    public void setProcessedDate(LocalDateTime processedDate) {
        this.processedDate = processedDate;
    }
    
    public LocalDateTime getCompletedDate() {
        return completedDate;
    }
    
    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }
}
