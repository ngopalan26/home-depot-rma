package com.homedepot.rma.model;

import com.homedepot.rma.enums.ReturnMethod;
import com.homedepot.rma.enums.ReturnReason;
import com.homedepot.rma.enums.ReturnStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "return_requests")
public class ReturnRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(unique = true)
    private String rmaNumber;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @Enumerated(EnumType.STRING)
    private ReturnReason reason;
    
    @Enumerated(EnumType.STRING)
    private ReturnMethod method;
    
    @Enumerated(EnumType.STRING)
    private ReturnStatus status;
    
    private String notes;
    private String trackingNumber;
    private String qrCodeData;
    private String shippingLabelUrl;
    
    @OneToMany(mappedBy = "returnRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReturnItem> returnItems;
    
    private LocalDateTime requestedDate;
    private LocalDateTime processedDate;
    private LocalDateTime completedDate;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public ReturnRequest() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.requestedDate = LocalDateTime.now();
        this.status = ReturnStatus.PENDING;
    }
    
    public ReturnRequest(String rmaNumber, Order order, Customer customer, ReturnReason reason, ReturnMethod method) {
        this();
        this.rmaNumber = rmaNumber;
        this.order = order;
        this.customer = customer;
        this.reason = reason;
        this.method = method;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getRmaNumber() {
        return rmaNumber;
    }
    
    public void setRmaNumber(String rmaNumber) {
        this.rmaNumber = rmaNumber;
    }
    
    public Order getOrder() {
        return order;
    }
    
    public void setOrder(Order order) {
        this.order = order;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
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
    
    public List<ReturnItem> getReturnItems() {
        return returnItems;
    }
    
    public void setReturnItems(List<ReturnItem> returnItems) {
        this.returnItems = returnItems;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
