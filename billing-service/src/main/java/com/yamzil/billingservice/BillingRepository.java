package com.yamzil.billingservice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Project: 2pc-demo
 * Updated by yamzil
 * Date: 1/29/2025
 * Time: 3:32 PM
 */
public interface BillingRepository extends JpaRepository<Billing, Long> {
    Optional<Billing> findByTransactionId(String transactionId);
}
