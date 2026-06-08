package com.elliotgaramendi.studentenrollment.infrastructure.adapter.out.persistence.repository;

import com.elliotgaramendi.studentenrollment.infrastructure.adapter.out.persistence.entity.StudentEnrollmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentEnrollmentJpaRepository extends JpaRepository<StudentEnrollmentEntity, Long> {

    List<StudentEnrollmentEntity> findByStudentId(Long studentId);

    void deleteByStudentId(Long studentId);
}
