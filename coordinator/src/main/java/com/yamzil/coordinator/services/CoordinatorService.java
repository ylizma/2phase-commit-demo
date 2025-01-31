package com.yamzil.coordinator.services;

import com.yamzil.coordinator.dtos.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Project: 2pc-demo
 * Updated by yamzil
 * Date: 1/30/2025
 * Time: 2:25 PM
 */
@Service
public class CoordinatorService {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    private final CoordinatorRepository coordinatorRepository;
    private final RestTemplate restTemplate;

    @Value("${student.service.url}")
    private String studentServiceUrl;
    @Value("${course.service.url}")
    private String courseServiceUrl;
    @Value("${billing.service.url}")
    private String billingServiceUrl;

    public CoordinatorService(CoordinatorRepository coordinatorRepository) {
        this.coordinatorRepository = coordinatorRepository;
        this.restTemplate = new RestTemplate();
    }

    @Transactional
    public ResponseEntity executeTransaction(TransactionDto transactionDto) {
        UUID transactionId = generateTransactionId();
        log.info("Starting transaction with ID: " + transactionId);

        saveTransaction(transactionId, FlowSatus.READY);
        log.info("Transaction saved with status: READY");

        if (!prepareStudent(transactionId, transactionDto.studentId())) {
            log.warning("Failed to prepare student with ID: " + transactionDto.studentId());
            return ResponseEntity.ok(new TransactionResponse(transactionId.toString(), FlowSatus.ROLLED_BACK));
        }
        log.info("Student prepared successfully for transaction ID: " + transactionId);

        if (!prepareCourse(transactionId, transactionDto.courseId())) {
            log.warning("Failed to prepare course with ID: " + transactionDto.courseId());
            rollbackStudent(transactionId, transactionDto.studentId());
            return ResponseEntity.ok(new TransactionResponse(transactionId.toString(), FlowSatus.ROLLED_BACK));
        }
        log.info("Course prepared successfully for transaction ID: " + transactionId);

        if (!prepareBilling(transactionId, transactionDto)) {
            log.warning("Failed to prepare billing for transaction ID: " + transactionId);
            rollbackStudent(transactionId, transactionDto.studentId());
            rollbackCourse(transactionId, transactionDto.courseId());
            return ResponseEntity.ok(new TransactionResponse(transactionId.toString(), FlowSatus.ROLLED_BACK));
        }
        log.info("Billing prepared successfully for transaction ID: " + transactionId);

        boolean committed = commitTransaction(transactionId, transactionDto);
        if (committed)
            return ResponseEntity.ok(new TransactionResponse(transactionId.toString(), FlowSatus.COMMITED));
        else
            return ResponseEntity.ok(new TransactionResponse(transactionId.toString(), FlowSatus.ROLLED_BACK));

    }

    private boolean prepareStudent(UUID transactionId, long studentId) {
        log.info("Preparing student with ID: " + studentId + " for transaction ID: " + transactionId);
        StudentPrepareRequest request = new StudentPrepareRequest(transactionId.toString(), studentId);
        return checkPrepareResponse(callService(studentServiceUrl, "/prepare", request));
    }

    private boolean prepareCourse(UUID transactionId, long courseId) {
        log.info("Preparing course with ID: " + courseId + " for transaction ID: " + transactionId);
        CoursePrepareRequest request = new CoursePrepareRequest(transactionId.toString(), courseId);
        return checkPrepareResponse(callService(courseServiceUrl, "/prepare", request));
    }

    private boolean prepareBilling(UUID transactionId, TransactionDto transactionDto) {
        log.info("Preparing billing for student ID: " + transactionDto.studentId() + " and amount: " + transactionDto.amount() + " for transaction ID: " + transactionId);
        BillingRequest request = new BillingRequest(transactionId.toString(), transactionDto.studentId(), transactionDto.amount());
        return checkPrepareResponse(callService(billingServiceUrl, "/prepare", request));
    }

    private boolean checkPrepareResponse(ResponseEntity<TransactionResponse> response) {
        log.info("Received response: " + response.toString());
        if (response.getStatusCode().is2xxSuccessful() && response.getBody().flowSatus().equals(FlowSatus.READY)) {
            return true;
        }
        log.warning("Prepare step failed, response status: " + response.getStatusCode() + " or flow status: " + response.getBody());
        return false;
    }

    private void rollbackStudent(UUID transactionId, long studentId) {
        log.info("Rolling back student with ID: " + studentId + " for transaction ID: " + transactionId);
        StudentPrepareRequest request = new StudentPrepareRequest(transactionId.toString(), studentId);
        callRollback(studentServiceUrl, request);
    }

    private void rollbackCourse(UUID transactionId, long courseId) {
        log.info("Rolling back course with ID: " + courseId + " for transaction ID: " + transactionId);
        CoursePrepareRequest request = new CoursePrepareRequest(transactionId.toString(), courseId);
        callRollback(courseServiceUrl, request);
    }

    private boolean commitTransaction(UUID transactionId, TransactionDto transactionDto) {
        log.info("Committing transaction with ID: " + transactionId);

        callCommit(studentServiceUrl, new StudentPrepareRequest(transactionId.toString(), transactionDto.studentId()));
        log.info("Committed student transaction for student ID: " + transactionDto.studentId());

        callCommit(courseServiceUrl, new CoursePrepareRequest(transactionId.toString(), transactionDto.courseId()));
        log.info("Committed course transaction for course ID: " + transactionDto.courseId());

        callCommit(billingServiceUrl, new BillingRequest(transactionId.toString(), transactionDto.studentId(), transactionDto.amount()));
        log.info("Committed billing transaction for student ID: " + transactionDto.studentId() + " and amount: " + transactionDto.amount());

        saveTransaction(transactionId, FlowSatus.COMMITED);
        log.info("Transaction committed with status: COMMITED");

        return true;
    }

    private UUID generateTransactionId() {
        UUID transactionId = UUID.randomUUID();
        log.info("Generated transaction ID: " + transactionId);
        return transactionId;
    }

    private boolean saveTransaction(UUID transactionId, FlowSatus status) {
        log.info("Saving transaction with ID: " + transactionId + " and status: " + status);
        return coordinatorRepository.findById(transactionId).map(tr -> {
            tr.setFlowSatus(status);
            coordinatorRepository.save(tr);
            return true;
        }).orElse(false);
    }

    private ResponseEntity<?> callCommit(String serviceUrl, Object request) {
        log.info("Calling commit endpoint for service URL: " + serviceUrl);
        return callService(serviceUrl, "/commit", request);
    }

    private ResponseEntity<?> callRollback(String serviceUrl, Object request) {
        log.info("Calling rollback endpoint for service URL: " + serviceUrl);
        return callService(serviceUrl, "/rollback", request);
    }

    private ResponseEntity<TransactionResponse> callService(String serviceUrl, String endPoint, Object request) {
        log.info("Calling service at URL: " + serviceUrl + endPoint + " with request: " + request);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<?> entity = new HttpEntity<>(request, headers);
            ResponseEntity<TransactionResponse> response = restTemplate.exchange(serviceUrl + endPoint, HttpMethod.POST, entity, TransactionResponse.class);
            log.info("Received response from service: " + response);
            return response;
        } catch (Exception e) {
            log.severe("Error calling service at " + serviceUrl + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


