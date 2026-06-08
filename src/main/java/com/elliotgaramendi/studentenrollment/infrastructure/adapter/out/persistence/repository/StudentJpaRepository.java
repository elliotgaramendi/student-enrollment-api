package com.elliotgaramendi.studentenrollment.infrastructure.adapter.out.persistence.repository;

import com.elliotgaramendi.studentenrollment.infrastructure.adapter.out.persistence.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentJpaRepository extends JpaRepository<StudentEntity, Long> {

    Optional<StudentEntity> findByEmail(String email);
}
