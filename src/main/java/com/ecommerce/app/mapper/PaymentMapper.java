package com.ecommerce.app.mapper;

import com.ecommerce.app.dto.PaymentDto;
import com.ecommerce.app.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(target = "paymentStatus", expression = "java(payment.getPaymentStatus() != null ? payment.getPaymentStatus().name() : null)")
    @Mapping(target = "paymentMethod", expression = "java(payment.getPaymentMethod() != null ? payment.getPaymentMethod().name() : null)")
    PaymentDto toDto(Payment payment);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "transactionId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "paymentStatus", expression = "java(dto.getPaymentStatus() != null ? com.ecommerce.app.entity.PaymentStatus.valueOf(dto.getPaymentStatus()) : com.ecommerce.app.entity.PaymentStatus.PENDING)")
    @Mapping(target = "paymentMethod", expression = "java(dto.getPaymentMethod() != null ? com.ecommerce.app.entity.PaymentMethod.valueOf(dto.getPaymentMethod()) : null)")
    Payment toEntity(PaymentDto dto);
}