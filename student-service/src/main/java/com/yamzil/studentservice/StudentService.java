package com.yamzil.studentservice;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Project: 2pc-demo
 * Updated by yamzil
 * Date: 1/29/2025
 * Time: 9:49 AM
 */

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public StudentDto saveStudent(StudentDto studentDto) {
        Student student = new Student(studentDto);
        studentRepository.save(student);
        return new StudentDto(student.getId(), student.getName(), student.getEmail(), student.getDateOfBirth());
    }

    public boolean isStudentExist(StudentPrepareRequest studentPrepareDto) {
        return studentRepository.findById(studentPrepareDto.studentId()).isPresent();
    }
}
