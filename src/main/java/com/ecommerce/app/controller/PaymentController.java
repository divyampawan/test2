package com.ecommerce.app.controller;

import com.ecommerce.app.dto.CreatePaymentRequestDto;
import com.ecommerce.app.dto.PaymentDto;
import com.ecommerce.app.entity.PaymentStatus;
import com.ecommerce.app.mapper.PaymentMapper;
import com.ecommerce.app.service.interfaces.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final IPaymentService paymentService;
    private final PaymentMapper paymentMapper;

    @Autowired
    public PaymentController(IPaymentService paymentService, PaymentMapper paymentMapper) {
        this.paymentService = paymentService;
        this.paymentMapper = paymentMapper;
    }

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody CreatePaymentRequestDto requestDto) {
        try {
            // Validate request
            if (requestDto == null || requestDto.getOrderId() == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Order ID is required");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            PaymentDto paymentDto = paymentService.createPayment(requestDto);
            return ResponseEntity.ok(paymentDto);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An unexpected error occurred while processing your payment");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping("/simulate-success")
    public ResponseEntity<?> simulateSuccessfulPayment(@RequestBody Map<String, String> payload) {
        try {
            // Validate request
            if (payload == null || !payload.containsKey("transactionId")) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Transaction ID is required");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            String transactionId = payload.get("transactionId").trim();
            if (transactionId.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Transaction ID cannot be empty");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            PaymentDto updatedPayment = paymentService.updatePaymentStatus(transactionId, PaymentStatus.SUCCESS);
            return ResponseEntity.ok(updatedPayment);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while processing the payment simulation");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
