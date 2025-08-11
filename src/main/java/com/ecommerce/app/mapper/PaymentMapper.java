package com.ecommerce.app.mapper;

import com.ecommerce.app.dto.PaymentDto;
import com.ecommerce.app.entity.Payment;
import com.ecommerce.app.entity.PaymentMethod;
import com.ecommerce.app.entity.PaymentStatus;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Payment entity and PaymentDto without MapStruct.
 */
@Component
public class PaymentMapper {

    /**
     * Converts a Payment entity to a PaymentDto.
     */
    public PaymentDto toDto(Payment payment) {
        if (payment == null) {
            return null;
        }

        PaymentDto dto = new PaymentDto();
        dto.setId(payment.getId());
        dto.setTransactionId(payment.getTransactionId());
        dto.setAmount(payment.getAmount());
        dto.setCurrency(payment.getCurrency());
        dto.setPaymentDetails(payment.getPaymentDetails());
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setUpdatedAt(payment.getUpdatedAt());

        if (payment.getOrder() != null) {
            dto.setOrderId(payment.getOrder().getId());
        }

        if (payment.getPaymentStatus() != null) {
            dto.setPaymentStatus(payment.getPaymentStatus().name());
        }
        if (payment.getPaymentMethod() != null) {
            dto.setPaymentMethod(payment.getPaymentMethod().name());
        }

        return dto;
    }

    /**
     * Converts a PaymentDto to a Payment entity.
     */
    public Payment toEntity(PaymentDto dto) {
        if (dto == null) {
            return null;
        }

        Payment payment = new Payment();
        // The Order object must be fetched and set in the service layer.
        payment.setId(dto.getId());

        // ✅ FIX: REMOVED this line. The transaction ID is generated automatically
        // by the Payment entity's @PrePersist method before it's saved.
        // payment.setTransactionId(dto.getTransactionId());

        payment.setAmount(dto.getAmount());
        payment.setPaymentDetails(dto.getPaymentDetails());

        if (dto.getPaymentStatus() != null) {
            payment.setPaymentStatus(PaymentStatus.valueOf(dto.getPaymentStatus()));
        }
        if (dto.getPaymentMethod() != null) {
            payment.setPaymentMethod(PaymentMethod.valueOf(dto.getPaymentMethod()));
        }

        return payment;
    }
}