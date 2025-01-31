package com.yamzil.studentservice;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * Project: 2pc-demo
 * Updated by yamzil
 * Date: 1/29/2025
 * Time: 9:48 AM
 */

@Entity
public class Student {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private String dateOfBirth;

    public Student(StudentDto studentDto) {
        this.name = studentDto.name();
        this.email = studentDto.email();
        this.dateOfBirth = studentDto.dateOfBirth();
    }

    public Student() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
