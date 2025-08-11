package com.ecommerce.app.service;

import com.ecommerce.app.dto.CreatePaymentRequestDto;
import com.ecommerce.app.dto.PaymentDto;
import com.ecommerce.app.entity.Order;
// import com.ecommerce.app.entity.OrderStatus; // REMOVED this import
import com.ecommerce.app.entity.Payment;
import com.ecommerce.app.entity.PaymentStatus;
import com.ecommerce.app.mapper.PaymentMapper;
import com.ecommerce.app.repository.OrderRepository;
import com.ecommerce.app.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;

    public PaymentService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository,
            PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.paymentMapper = paymentMapper;
    }

    @Transactional
    public PaymentDto createPayment(CreatePaymentRequestDto requestDto) {
        Order order = orderRepository.findById(requestDto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + requestDto.getOrderId()));

        // CHANGED: Compare with a String instead of an enum
        if (!"PENDING".equals(order.getStatus())) {
            throw new IllegalStateException("Order is not in a payable state. Current status: " + order.getStatus());
        }

        Payment payment = new Payment(
            order,
            order.getTotalAmount(),
            requestDto.getPaymentDetails(),
            requestDto.getPaymentMethod()
        );

        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toDto(savedPayment);
    }

    @Transactional
    public PaymentDto updatePaymentStatus(String transactionId, PaymentStatus newStatus) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found with transaction ID: " + transactionId));

        payment.setPaymentStatus(newStatus);

        Order order = payment.getOrder();
        if (newStatus == PaymentStatus.SUCCESS) {
            // CHANGED: Set status using a String
            order.setStatus("PAID");
        } else if (newStatus == PaymentStatus.FAILED) {
            // CHANGED: Set status using a String
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