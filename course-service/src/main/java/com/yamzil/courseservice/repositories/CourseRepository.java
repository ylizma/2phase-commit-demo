package com.yamzil.courseservice.repositories;

import com.yamzil.courseservice.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Project: 2pc-demo
 * Updated by yamzil
 * Date: 1/29/2025
 * Time: 2:25 PM
 */

public interface CourseRepository extends JpaRepository<Course, Long> {
}
