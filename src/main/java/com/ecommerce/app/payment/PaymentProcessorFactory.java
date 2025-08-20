package com.ecommerce.app.payment;
import com.ecommerce.app.entity.PaymentMethod;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PaymentProcessorFactory {

    private final Map<PaymentMethod, PaymentProcessor> processorMap;

    public PaymentProcessorFactory(List<PaymentProcessor> processors) {
        this.processorMap = processors.stream()
                .collect(Collectors.toMap(PaymentProcessor::getHandledPaymentMethod, Function.identity()));
    }

    public PaymentProcessor getProcessor(PaymentMethod paymentMethod) {
        PaymentProcessor processor = processorMap.get(paymentMethod);
        if (processor == null) {
            throw new IllegalArgumentException("No payment processor found for method: " + paymentMethod);
        }
        return processor;
    }
}
