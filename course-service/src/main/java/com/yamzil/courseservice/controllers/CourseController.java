package com.yamzil.courseservice.controllers;

import com.yamzil.courseservice.dtos.CourseDto;
import com.yamzil.courseservice.dtos.CoursePrepareRequest;
import com.yamzil.courseservice.dtos.CoursePrepareResponse;
import com.yamzil.courseservice.dtos.FlowSatus;
import com.yamzil.courseservice.services.CourseService;
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
 * Time: 2:21 PM
 */

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseDto> saveCourse(@RequestBody CourseDto courseDto) {
        return ResponseEntity.ok(courseService.saveCourse(courseDto));
    }

    @PostMapping("/prepare")
    public ResponseEntity<CoursePrepareResponse> prepare(@RequestBody CoursePrepareRequest coursePrepareRequest) {
        boolean isEnrolled = courseService.isCourseEnrolled(coursePrepareRequest);
        if (isEnrolled) {
            return ResponseEntity.ok(new CoursePrepareResponse(coursePrepareRequest.transactionId(), FlowSatus.READY));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CoursePrepareResponse(coursePrepareRequest.transactionId(), FlowSatus.ROLLED_BACK));
        }
    }

    @PostMapping("/commit")
    public ResponseEntity<CoursePrepareResponse> commit(@RequestBody CoursePrepareRequest coursePrepareRequest) {
        return ResponseEntity.ok(new CoursePrepareResponse(coursePrepareRequest.transactionId(), FlowSatus.COMMITED));
    }

    @PostMapping("/rollback")
    public ResponseEntity<CoursePrepareResponse> rollback(@RequestBody CoursePrepareRequest coursePrepareRequest) {
        courseService.rollbackCourseEnrollement(coursePrepareRequest);
        return ResponseEntity.ok(new CoursePrepareResponse(coursePrepareRequest.transactionId(), FlowSatus.ROLLED_BACK));
    }
}
