package com.ecommerce.app.service.interfaces;

import com.ecommerce.app.dto.CreatePaymentRequestDto;
import com.ecommerce.app.dto.PaymentDto;
import com.ecommerce.app.entity.PaymentStatus;

public interface IPaymentService {
    PaymentDto createPayment(CreatePaymentRequestDto requestDto);
    PaymentDto updatePaymentStatus(String transactionId, PaymentStatus newStatus);
    PaymentDto getPaymentByTransactionId(String transactionId);
}
