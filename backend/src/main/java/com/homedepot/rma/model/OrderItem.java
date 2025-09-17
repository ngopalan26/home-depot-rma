package com.homedepot.rma.model;

import com.homedepot.rma.enums.ProductCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    
    @NotBlank
    private String productId;
    
    @NotBlank
    private String productName;
    
    private String productDescription;
    
    @NotBlank
    private String sku;
    
    @NotNull
    @Positive
    private Integer quantity;
    
    @NotNull
    @Positive
    private BigDecimal unitPrice;
    
    @NotNull
    @Positive
    private BigDecimal totalPrice;
    
    @Enumerated(EnumType.STRING)
    private ProductCategory category;
    
    private Boolean isLargeItem;
    private Boolean isHazardous;
    
    // Constructors
    public OrderItem() {}
    
    public OrderItem(Order order, String productId, String productName, String sku, 
                    Integer quantity, BigDecimal unitPrice, ProductCategory category) {
        this.order = order;
        this.productId = productId;
        this.productName = productName;
        this.sku = sku;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        this.category = category;
        this.isLargeItem = false;
        this.isHazardous = false;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Order getOrder() {
        return order;
    }
    
    public void setOrder(Order order) {
        this.order = order;
    }
    
    public String getProductId() {
        return productId;
    }
    
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getProductDescription() {
        return productDescription;
    }
    
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
    
    public String getSku() {
        return sku;
    }
    
    public void setSku(String sku) {
        this.sku = sku;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        if (this.unitPrice != null) {
            this.totalPrice = this.unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        if (this.quantity != null) {
            this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(this.quantity));
        }
    }
    
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public ProductCategory getCategory() {
        return category;
    }
    
    public void setCategory(ProductCategory category) {
        this.category = category;
    }
    
    public Boolean getIsLargeItem() {
        return isLargeItem;
    }
    
    public void setIsLargeItem(Boolean isLargeItem) {
        this.isLargeItem = isLargeItem;
    }
    
    public Boolean getIsHazardous() {
        return isHazardous;
    }
    
    public void setIsHazardous(Boolean isHazardous) {
        this.isHazardous = isHazardous;
    }
}
