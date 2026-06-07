package com.example.studentenrollment.domain.port.out;

import com.example.studentenrollment.domain.model.StudentEnrollment;

import java.util.List;
import java.util.Optional;

public interface StudentEnrollmentRepositoryPort {

    StudentEnrollment save(StudentEnrollment enrollment);

    Optional<StudentEnrollment> findById(Long id);

    List<StudentEnrollment> findAll();

    List<StudentEnrollment> findByStudentId(Long studentId);

    void deleteById(Long id);

    void deleteByStudentId(Long studentId);
}
