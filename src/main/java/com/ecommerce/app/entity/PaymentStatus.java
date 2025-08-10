package com.ecommerce.app.entity;

/**
 * Represents the status of a payment transaction in the e-commerce system.
 * Possible values are:
 * - PENDING: Initial state when payment is being processed
 * - SUCCESS: Payment was successfully processed
 * - FAILED: Payment processing failed
 * - REFUNDED: Payment was refunded to the customer
 */
public enum PaymentStatus {
    PENDING,
    SUCCESS,
    FAILED,
    REFUNDED
}
