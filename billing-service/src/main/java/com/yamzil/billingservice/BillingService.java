package com.yamzil.billingservice;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Project: 2pc-demo
 * Updated by yamzil
 * Date: 1/29/2025
 * Time: 3:24 PM
 */

@Service
public class BillingService {

    private final BillingRepository billingRepository;

    public BillingService(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    @Transactional
    public boolean executePayment(BillingRequest billingDto) {
        double result = billingDto.amount() * Math.random();
        if (result >= 100) {
            billingRepository.save(new Billing(billingDto.studentId(), billingDto.amount(), PaymentStatus.PENDING, billingDto.transactionId()));
            return true;
        } else {
            billingRepository.save(new Billing(billingDto.studentId(), billingDto.amount(), PaymentStatus.CANCELED, billingDto.transactionId()));
            return false;
        }
    }

    @Transactional
    public boolean validatePayment(BillingRequest billingDto) {
        return billingRepository.findByTransactionId(billingDto.transactionId()).map(billing -> {
            billing.setPaymentStatus(PaymentStatus.PAID);
            billingRepository.save(billing);
            return true;
        }).orElse(false);
    }
}
