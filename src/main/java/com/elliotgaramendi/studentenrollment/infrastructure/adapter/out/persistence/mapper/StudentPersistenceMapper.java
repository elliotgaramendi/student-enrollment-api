package com.elliotgaramendi.studentenrollment.infrastructure.adapter.out.persistence.mapper;

import com.elliotgaramendi.studentenrollment.domain.model.Student;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.out.persistence.entity.StudentEntity;
import org.springframework.stereotype.Component;

@Component
public class StudentPersistenceMapper {

    public StudentEntity toEntity(Student student) {
        return new StudentEntity(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail()
        );
    }

    public Student toDomain(StudentEntity entity) {
        return new Student(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail()
        );
    }
}
