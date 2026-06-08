package com.elliotgaramendi.studentenrollment.infrastructure.adapter.out.persistence.mapper;

import com.elliotgaramendi.studentenrollment.domain.model.StudentEnrollment;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.out.persistence.entity.StudentEntity;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.out.persistence.entity.StudentEnrollmentEntity;
import org.springframework.stereotype.Component;

@Component
public class StudentEnrollmentPersistenceMapper {

    public StudentEnrollmentEntity toEntity(StudentEnrollment enrollment, StudentEntity studentEntity) {
        return new StudentEnrollmentEntity(
                enrollment.getId(),
                studentEntity,
                enrollment.getCourseCode(),
                enrollment.getEnrollmentDate()
        );
    }

    public StudentEnrollment toDomain(StudentEnrollmentEntity entity) {
        return new StudentEnrollment(
                entity.getId(),
                entity.getStudent().getId(),
                entity.getCourseCode(),
                entity.getEnrollmentDate()
        );
    }
}
