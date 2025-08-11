package com.ecommerce.app.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a payment transaction in the e-commerce system.
 * Each payment is associated with exactly one order.
 */
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    // ✨ REMOVED updatable=false to avoid potential issues with some JPA providers
    // The transactionId is set only once in the constructor or pre-persist logic anyway.
    @Column(name = "transaction_id", nullable = false, unique = true, length = 50)
    private String transactionId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    // This field can store details like the last 4 digits of a card.
    @Column(name = "payment_details", length = 100)
    private String paymentDetails;

    @Column(nullable = false, length = 3)
    private String currency = "USD";

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    // ✨ Using @Column(updatable = false) is a more standard JPA way
    // to ensure this field is only set on creation.
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Payment() {
        // Default constructor for JPA
    }

    public Payment(Order order, BigDecimal amount, String paymentDetails, PaymentMethod paymentMethod) {
        if (order == null || amount == null || paymentMethod == null) {
            throw new IllegalArgumentException("Order, amount, and payment method cannot be null");
        }
        this.order = order;
        this.amount = amount;
        this.paymentDetails = paymentDetails;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = PaymentStatus.PENDING; // Default status
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public String getTransactionId() { return transactionId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getPaymentDetails() { return paymentDetails; }
    public void setPaymentDetails(String paymentDetails) { this.paymentDetails = paymentDetails; }
    public String getCurrency() { return currency; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    /**
     * JPA lifecycle callback to set timestamps and transaction ID before saving.
     */
    @PrePersist
    protected void onPrePersist() {
        this.createdAt = LocalDateTime.now();
        // Generate a unique transaction ID only if it hasn't been set.
        if (this.transactionId == null) {
            this.transactionId = "PAY-" + System.currentTimeMillis() + "-" + (long) (Math.random() * 9000L + 1000L);
        }
    }

    /**
     * JPA lifecycle callback to set the update timestamp before updating.
     */
    @PreUpdate
    protected void onPreUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", orderId=" + (order != null ? order.getId() : "null") +
                ", transactionId='" + transactionId + '\'' +
                ", amount=" + amount +
                ", paymentStatus=" + paymentStatus +
                '}';
    }
}