package com.yamzil.coordinator.models;

import com.yamzil.coordinator.dtos.FlowSatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Project: 2pc-demo
 * Updated by yamzil
 * Date: 1/30/2025
 * Time: 5:41 PM
 */

@Entity
public class CourseTransaction {
    @Id
    private UUID id;
    private FlowSatus flowSatus;
    @CreatedDate
    private Timestamp createdAt;

    public CourseTransaction() {
    }

    public CourseTransaction(UUID id, FlowSatus flowSatus) {
        this.id = id;
        this.flowSatus = flowSatus;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public FlowSatus getFlowSatus() {
        return flowSatus;
    }

    public void setFlowSatus(FlowSatus flowSatus) {
        this.flowSatus = flowSatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
