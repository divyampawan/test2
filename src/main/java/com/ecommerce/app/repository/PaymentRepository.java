package com.ecommerce.app.repository;

import com.ecommerce.app.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing Payment entities in the database.
 * Provides CRUD operations and custom query methods for Payment entities.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    /**
     * Finds a payment by its associated order ID.
     *
     * @param orderId the ID of the order
     * @return an Optional containing the payment if found, or empty if not found
     */
    Optional<Payment> findByOrderId(Long orderId);
    
    /**
     * Checks if a payment exists for the given transaction ID.
     *
     * @param transactionId the transaction ID to check
     * @return true if a payment with the given transaction ID exists, false otherwise
     */
    boolean existsByTransactionId(String transactionId);
    
    /**
     * Finds a payment by its transaction ID.
     *
     * @param transactionId the transaction ID to search for
     * @return an Optional containing the payment if found, or empty if not found
     */
    Optional<Payment> findByTransactionId(String transactionId);
}
