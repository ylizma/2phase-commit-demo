package com.yamzil.studentservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project: 2pc-demo
 * Updated by yamzil
 * Date: 1/29/2025
 * Time: 9:50 AM
 */
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentDto> saveStudent(@RequestBody StudentDto studentDto) {
        return ResponseEntity.ok(studentService.saveStudent(studentDto));
    }

    @PostMapping(value = "/prepare", consumes = "application/json", produces = "application/json")
    public ResponseEntity<StudentPrepareResponse> prepare(@RequestBody StudentPrepareRequest studentPrepareRequest) {
        boolean exists = studentService.isStudentExist(studentPrepareRequest);
        if (exists) {
            return ResponseEntity.ok(new StudentPrepareResponse(studentPrepareRequest.transactioId(), FlowSatus.READY));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new StudentPrepareResponse(studentPrepareRequest.transactioId(), FlowSatus.ROLLED_BACK));
        }
    }

    @PostMapping("/commit")
    public ResponseEntity<StudentPrepareResponse> commit(@RequestBody StudentPrepareRequest studentPrepareRequest) {
        return ResponseEntity.ok(new StudentPrepareResponse(studentPrepareRequest.transactioId(), FlowSatus.COMMITED));
    }

    @PostMapping("/rollback")
    public ResponseEntity<StudentPrepareResponse> rollback(@RequestBody StudentPrepareRequest studentPrepareRequest) {
        return ResponseEntity.ok(new StudentPrepareResponse(studentPrepareRequest.transactioId(), FlowSatus.ROLLED_BACK));
    }
}
