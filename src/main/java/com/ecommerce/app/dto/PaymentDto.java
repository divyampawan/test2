package com.ecommerce.app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for representing Payment data in API responses and requests.
 */
public class PaymentDto {

    private Long id;
    private Long orderId;
    private String transactionId;
    private BigDecimal amount;
    private String currency;
    private String paymentStatus;
    private String paymentMethod;
    private String paymentDetails;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public PaymentDto() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getPaymentDetails() { return paymentDetails; }
    public void setPaymentDetails(String paymentDetails) { this.paymentDetails = paymentDetails; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "PaymentDto{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", transactionId='" + transactionId + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentDetails='" + (paymentDetails != null ? "[PROTECTED]" : null) + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
