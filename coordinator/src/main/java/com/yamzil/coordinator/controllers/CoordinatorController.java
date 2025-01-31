package com.yamzil.coordinator.controllers;

import com.yamzil.coordinator.dtos.TransactionDto;
import com.yamzil.coordinator.services.CoordinatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project: 2pc-demo
 * Updated by yamzil
 * Date: 1/30/2025
 * Time: 2:23 PM
 */

@RestController
@RequestMapping("/transactions")
public class CoordinatorController {

    private final CoordinatorService coordinatorService;

    public CoordinatorController(CoordinatorService coordinatorService) {
        this.coordinatorService = coordinatorService;
    }

    @PostMapping
    public ResponseEntity execute(@RequestBody TransactionDto transactionDto) {
        return  coordinatorService.executeTransaction(transactionDto);
    }
}
