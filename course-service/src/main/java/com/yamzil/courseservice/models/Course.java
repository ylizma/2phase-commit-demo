package com.yamzil.courseservice.models;

import com.yamzil.courseservice.dtos.CourseDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * Project: 2pc-demo
 * Updated by yamzil
 * Date: 1/29/2025
 * Time: 2:22 PM
 */

@Entity
public class Course {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Integer seatsAvailable;

    public Course(CourseDto courseDto) {
        this.name = courseDto.name();
        this.seatsAvailable = courseDto.seatsAvailable();
    }

    public Course() {
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

    public Integer getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(Integer seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }
}
