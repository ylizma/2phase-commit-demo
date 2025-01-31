package com.yamzil.courseservice.services;

import com.yamzil.courseservice.dtos.CourseDto;
import com.yamzil.courseservice.dtos.CoursePrepareRequest;
import com.yamzil.courseservice.models.Course;
import com.yamzil.courseservice.repositories.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Project: 2pc-demo
 * Updated by yamzil
 * Date: 1/29/2025
 * Time: 2:24 PM
 */

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private static final int MAX_COURSE_SEAT_NUMBER = 10;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional
    public CourseDto saveCourse(CourseDto courseDto) {
        Course course = courseRepository.save(new Course(courseDto));
        return new CourseDto(course.getId(), course.getName(), course.getSeatsAvailable());
    }

    @Transactional
    public boolean isCourseEnrolled(CoursePrepareRequest coursePrepareRequest) {
        return courseRepository.findById(coursePrepareRequest.courseId()).map(course -> {
            if (course.getSeatsAvailable() > 0) {
                course.setSeatsAvailable(course.getSeatsAvailable() - 1);
                courseRepository.save(course);
                return true;
            }
            return false;
        }).orElse(false);
    }

    @Transactional
    public boolean rollbackCourseEnrollement(CoursePrepareRequest coursePrepareRequest) {
        return courseRepository.findById(coursePrepareRequest.courseId()).map(course -> {
            course.setSeatsAvailable(course.getSeatsAvailable() + 1);
            courseRepository.save(course);
            return true;
        }).orElse(false);
    }
}
