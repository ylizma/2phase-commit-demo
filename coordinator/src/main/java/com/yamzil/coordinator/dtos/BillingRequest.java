package com.yamzil.coordinator.dtos;

/**
 * Project: 2pc-demo
 * Updated by yamzil
 * Date: 1/29/2025
 * Time: 3:23 PM
 */
public record BillingRequest(String transactionId, Long studentId, Double amount) {
}
