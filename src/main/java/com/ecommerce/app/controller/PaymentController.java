package com.ecommerce.app.controller;

import com.ecommerce.app.dto.CreatePaymentRequestDto;
import com.ecommerce.app.dto.PaymentDto;
import com.ecommerce.app.entity.PaymentStatus;
import com.ecommerce.app.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// Temporarily disabled - using ApiController instead
//@RestController
//@RequestMapping("/api/v1/payments")
//@CrossOrigin(origins = "*")
@Deprecated
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(@RequestBody CreatePaymentRequestDto requestDto) {
        try {
            PaymentDto paymentDto = paymentService.createPayment(requestDto);
            return ResponseEntity.ok(paymentDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/simulate-success")
    public ResponseEntity<PaymentDto> simulateSuccessfulPayment(@RequestBody Map<String, String> payload) {
        try {
            String transactionId = payload.get("transactionId");
            if (transactionId == null || transactionId.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            PaymentDto updatedPayment = paymentService.updatePaymentStatus(transactionId, PaymentStatus.SUCCESS);
            return ResponseEntity.ok(updatedPayment);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
