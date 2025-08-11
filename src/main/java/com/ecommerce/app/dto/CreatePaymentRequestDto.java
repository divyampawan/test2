package com.ecommerce.app.dto;

import com.ecommerce.app.entity.PaymentMethod;

/**
 * DTO for the request to create a new payment.
 */
public class CreatePaymentRequestDto {

    private Long orderId;
    private PaymentMethod paymentMethod;
    private String paymentDetails; // e.g., "Card ending in 1234"

    // Getters and Setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { 
        this.paymentMethod = paymentMethod; 
    }
    
    public String getPaymentDetails() { return paymentDetails; }
    public void setPaymentDetails(String paymentDetails) { 
        this.paymentDetails = paymentDetails; 
    }

    @Override
    public String toString() {
        return "CreatePaymentRequestDto{" +
                "orderId=" + orderId +
                ", paymentMethod=" + paymentMethod +
                ", paymentDetails='" + (paymentDetails != null ? "[PROTECTED]" : null) + '\'' +
                '}';
    }
}
