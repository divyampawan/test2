package com.ecommerce.app.entity;

/**
 * Represents the different payment methods available in the e-commerce system.
 * Supported payment methods are:
 * - CARD: Credit/Debit card payments
 * - UPI: Unified Payments Interface (e.g., Google Pay, PhonePe)
 * - WALLET: Digital wallet payments (e.g., Paytm Wallet, Amazon Pay)
 * - NET_BANKING: Direct bank transfers
 */
public enum PaymentMethod {
    CARD,
    UPI,
    WALLET,
    NET_BANKING
}
