package com.ecommerce.app.dto;

import com.ecommerce.app.entity.PaymentMethod;

public class CreatePaymentRequestDto {

    private Long orderId;
    private PaymentMethod paymentMethod;
    private String paymentDetails; 

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
