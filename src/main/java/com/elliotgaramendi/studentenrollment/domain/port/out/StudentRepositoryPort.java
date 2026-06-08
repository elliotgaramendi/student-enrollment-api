package com.elliotgaramendi.studentenrollment.domain.port.out;

import com.elliotgaramendi.studentenrollment.domain.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepositoryPort {

    Student save(Student student);

    Optional<Student> findById(Long id);

    List<Student> findAll();

    boolean existsById(Long id);

    Optional<Student> findByEmail(String email);

    void deleteById(Long id);
}
