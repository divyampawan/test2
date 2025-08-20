package com.ecommerce.app.service;

import com.ecommerce.app.dto.CreatePaymentRequestDto;
import com.ecommerce.app.dto.PaymentDto;
import com.ecommerce.app.entity.Order;
import com.ecommerce.app.entity.Payment;
import com.ecommerce.app.entity.PaymentStatus;
import com.ecommerce.app.mapper.PaymentMapper;
import com.ecommerce.app.payment.PaymentProcessor;
import com.ecommerce.app.payment.PaymentProcessorFactory;
import com.ecommerce.app.repository.OrderRepository;
import com.ecommerce.app.repository.PaymentRepository;
import com.ecommerce.app.service.interfaces.IPaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService implements IPaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentProcessorFactory paymentFactory; // <-- 1. Injected the factory

    public PaymentService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository,
            PaymentMapper paymentMapper,
            PaymentProcessorFactory paymentFactory) { // <-- 2. Updated the constructor
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.paymentMapper = paymentMapper;
        this.paymentFactory = paymentFactory;
    }

    @Transactional
    public PaymentDto createPayment(CreatePaymentRequestDto requestDto) {
        // Find the order that needs to be paid
        Order order = orderRepository.findById(requestDto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + requestDto.getOrderId()));

        // Check if the order is in a state that allows payment
        if (!"PENDING".equals(order.getStatus())) {
            throw new IllegalStateException("Order is not in a payable state. Current status: " + order.getStatus());
        }

        // --- FACTORY PATTERN IN ACTION ---
        // 3. Get the correct processor from the factory based on the payment method
        PaymentProcessor processor = paymentFactory.getProcessor(requestDto.getPaymentMethod());

        // 4. Delegate the actual payment processing logic to the selected processor
        boolean isPaymentSuccessful = processor.processPayment(order);
        // ---------------------------------

        // Create the Payment record based on the outcome
        Payment payment = new Payment(
                order,
                order.getTotalAmount(),
                requestDto.getPaymentDetails(),
                requestDto.getPaymentMethod()
        );

        // Update statuses based on whether the payment succeeded or failed
        if (isPaymentSuccessful) {
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            order.setStatus("PAID");
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            order.setStatus("PAYMENT_FAILED");
        }

        // Save the updated order and the new payment record
        orderRepository.save(order);
        Payment savedPayment = paymentRepository.save(payment);

        // Return the DTO of the newly created payment
        return paymentMapper.toDto(savedPayment);
    }

    @Transactional
    public PaymentDto updatePaymentStatus(String transactionId, PaymentStatus newStatus) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found with transaction ID: " + transactionId));

        payment.setPaymentStatus(newStatus);

        Order order = payment.getOrder();
        if (newStatus == PaymentStatus.SUCCESS) {
            order.setStatus("PAID");
        } else if (newStatus == PaymentStatus.FAILED) {
            order.setStatus("PAYMENT_FAILED");
        }

        orderRepository.save(order);
        Payment updatedPayment = paymentRepository.save(payment);

        return paymentMapper.toDto(updatedPayment);
    }

    public PaymentDto getPaymentByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId)
                .map(paymentMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Payment not found with transaction ID: " + transactionId));
    }
}