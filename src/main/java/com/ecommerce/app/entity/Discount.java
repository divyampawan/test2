package com.ecommerce.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero; // Import the correct annotation
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "discounts")
public class Discount {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Discount code is required")
    @Column(unique = true)
    private String code;
    
    @NotBlank(message = "Discount description is required")
    private String description;
    
    @NotNull(message = "Discount percentage is required")
    // *** THE FIX IS HERE: Changed from @Positive to @PositiveOrZero ***
    @PositiveOrZero(message = "Discount percentage must be zero or positive")
    private BigDecimal percentage;
    
    @NotNull(message = "Minimum order amount is required")
    @PositiveOrZero(message = "Minimum order amount must be zero or positive")
    private BigDecimal minimumOrderAmount;
    
    @NotNull(message = "Maximum discount amount is required")
    @Positive(message = "Maximum discount amount must be positive")
    private BigDecimal maxDiscountAmount;
    
    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;
    
    @NotNull(message = "End date is required")
    private LocalDateTime endDate;
    
    private boolean active = true;
    
    private int usageLimit;
    
    private int usageCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Discount() {}
    
    public Discount(String code, String description, BigDecimal percentage, 
                   BigDecimal minimumOrderAmount, BigDecimal maxDiscountAmount,
                   LocalDateTime startDate, LocalDateTime endDate, int usageLimit) {
        this.code = code;
        this.description = description;
        this.percentage = percentage;
        this.minimumOrderAmount = minimumOrderAmount;
        this.maxDiscountAmount = maxDiscountAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.usageLimit = usageLimit;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getPercentage() {
        return percentage;
    }
    
    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }
    
    public BigDecimal getMinimumOrderAmount() {
        return minimumOrderAmount;
    }
    
    public void setMinimumOrderAmount(BigDecimal minimumOrderAmount) {
        this.minimumOrderAmount = minimumOrderAmount;
    }
    
    public BigDecimal getMaxDiscountAmount() {
        return maxDiscountAmount;
    }
    
    public void setMaxDiscountAmount(BigDecimal maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }
    
    public LocalDateTime getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    
    public LocalDateTime getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public int getUsageLimit() {
        return usageLimit;
    }
    
    public void setUsageLimit(int usageLimit) {
        this.usageLimit = usageLimit;
    }
    
    public int getUsageCount() {
        return usageCount;
    }
    
    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }
    
    // Business methods
    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        return active && 
               now.isAfter(startDate) && 
               now.isBefore(endDate) && 
               (usageLimit == 0 || usageCount < usageLimit);
    }
    
    public BigDecimal calculateDiscount(BigDecimal orderAmount) {
        if (!isValid() || orderAmount.compareTo(minimumOrderAmount) < 0) {
            return BigDecimal.ZERO;
        }
        
        // Handle flat discount (when percentage is 0)
        if (percentage.compareTo(BigDecimal.ZERO) == 0) {
            return maxDiscountAmount;
        }
        
        // Handle percentage discount
        BigDecimal discountAmount = orderAmount.multiply(percentage).divide(BigDecimal.valueOf(100));
        return discountAmount.compareTo(maxDiscountAmount) > 0 ? maxDiscountAmount : discountAmount;
    }
    
    public void incrementUsage() {
        this.usageCount++;
    }
}
