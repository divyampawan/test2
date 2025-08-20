package com.ecommerce.app.payment;
import com.ecommerce.app.entity.Order;
import com.ecommerce.app.entity.PaymentMethod;
import org.springframework.stereotype.Component;

@Component
public class CardPaymentProcessor implements PaymentProcessor {

    @Override
    public boolean processPayment(Order order) {
        System.out.println("Processing CARD payment for order: " + order.getId());
        return true;
    }
    @Override
    public PaymentMethod getHandledPaymentMethod() {
        return PaymentMethod.CARD;
    }
}
