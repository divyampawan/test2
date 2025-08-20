package com.ecommerce.app.payment;
import com.ecommerce.app.entity.Order;
import com.ecommerce.app.entity.PaymentMethod;

public interface PaymentProcessor {

    boolean processPayment(Order order);
    PaymentMethod getHandledPaymentMethod();
}
