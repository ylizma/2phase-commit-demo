package com.yamzil.coordinator.services;

import com.yamzil.coordinator.models.CourseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Project: 2pc-demo
 * Updated by yamzil
 * Date: 1/30/2025
 * Time: 5:48 PM
 */
public interface CoordinatorRepository extends JpaRepository<CourseTransaction, UUID> {
}
