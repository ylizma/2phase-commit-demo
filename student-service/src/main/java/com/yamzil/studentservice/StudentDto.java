package com.yamzil.studentservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;

/**
 * Project: 2pc-demo
 * Updated by yamzil
 * Date: 1/29/2025
 * Time: 9:51 AM
 */

public record StudentDto(Long id, String name, String email, String dateOfBirth) {
}
