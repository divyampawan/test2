package com.ecommerce.app.payment;
import com.ecommerce.app.entity.Order;
import com.ecommerce.app.entity.PaymentMethod;
import org.springframework.stereotype.Component;

@Component
public class UpiPaymentProcessor implements PaymentProcessor {

    @Override
    public boolean processPayment(Order order) {
        System.out.println("Processing UPI payment for order: " + order.getId());
        return true;
    }

    @Override
    public PaymentMethod getHandledPaymentMethod() {
        return PaymentMethod.UPI;
    }
}