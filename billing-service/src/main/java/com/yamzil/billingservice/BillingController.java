package com.yamzil.billingservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project: 2pc-demo
 * Updated by yamzil
 * Date: 1/29/2025
 * Time: 3:22 PM
 */

@RestController
@RequestMapping("/billing")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostMapping("/prepare")
    public ResponseEntity<BillingResponse> bill(@RequestBody BillingRequest billingRequest) {
        boolean isExecuted = billingService.executePayment(billingRequest);
        if (isExecuted) {
            return ResponseEntity.ok(new BillingResponse(billingRequest.transactionId(), FlowSatus.READY));
        } else {
            return ResponseEntity.ok(new BillingResponse(billingRequest.transactionId(), FlowSatus.ROLLED_BACK));
        }
    }

    @PostMapping("/commit")
    public ResponseEntity<BillingResponse> commit(@RequestBody BillingRequest billingRequest) {
        billingService.validatePayment(billingRequest);
        return ResponseEntity.ok(new BillingResponse(billingRequest.transactionId(), FlowSatus.COMMITED));
    }
}
