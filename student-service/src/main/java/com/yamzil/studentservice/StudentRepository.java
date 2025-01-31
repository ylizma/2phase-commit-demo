package com.yamzil.studentservice;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Project: 2pc-demo
 * Updated by yamzil
 * Date: 1/29/2025
 * Time: 9:49 AM
 */

public interface StudentRepository extends JpaRepository<Student, Long> {
}
