package com.elliotgaramendi.studentenrollment.infrastructure.adapter.out.persistence.adapter;

import com.elliotgaramendi.studentenrollment.domain.model.StudentEnrollment;
import com.elliotgaramendi.studentenrollment.domain.port.out.StudentEnrollmentRepositoryPort;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.out.persistence.entity.StudentEntity;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.out.persistence.mapper.StudentEnrollmentPersistenceMapper;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.out.persistence.repository.StudentEnrollmentJpaRepository;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.out.persistence.repository.StudentJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class StudentEnrollmentPersistenceAdapter implements StudentEnrollmentRepositoryPort {

    private final StudentEnrollmentJpaRepository studentEnrollmentJpaRepository;
    private final StudentJpaRepository studentJpaRepository;
    private final StudentEnrollmentPersistenceMapper studentEnrollmentPersistenceMapper;

    public StudentEnrollmentPersistenceAdapter(
            StudentEnrollmentJpaRepository studentEnrollmentJpaRepository,
            StudentJpaRepository studentJpaRepository,
            StudentEnrollmentPersistenceMapper studentEnrollmentPersistenceMapper
    ) {
        this.studentEnrollmentJpaRepository = studentEnrollmentJpaRepository;
        this.studentJpaRepository = studentJpaRepository;
        this.studentEnrollmentPersistenceMapper = studentEnrollmentPersistenceMapper;
    }

    @Override
    public StudentEnrollment save(StudentEnrollment enrollment) {
        StudentEntity studentEntity = studentJpaRepository.getReferenceById(enrollment.getStudentId());
        return studentEnrollmentPersistenceMapper.toDomain(
                studentEnrollmentJpaRepository.save(
                        studentEnrollmentPersistenceMapper.toEntity(enrollment, studentEntity)
                )
        );
    }

    @Override
    public Optional<StudentEnrollment> findById(Long id) {
        return studentEnrollmentJpaRepository.findById(id).map(studentEnrollmentPersistenceMapper::toDomain);
    }

    @Override
    public List<StudentEnrollment> findAll() {
        return studentEnrollmentJpaRepository.findAll()
                .stream()
                .map(studentEnrollmentPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<StudentEnrollment> findByStudentId(Long studentId) {
        return studentEnrollmentJpaRepository.findByStudentId(studentId)
                .stream()
                .map(studentEnrollmentPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        studentEnrollmentJpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByStudentId(Long studentId) {
        studentEnrollmentJpaRepository.deleteByStudentId(studentId);
    }
}
