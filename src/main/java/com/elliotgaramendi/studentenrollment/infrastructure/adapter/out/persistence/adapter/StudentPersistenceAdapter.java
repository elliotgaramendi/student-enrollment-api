package com.elliotgaramendi.studentenrollment.infrastructure.adapter.out.persistence.adapter;

import com.elliotgaramendi.studentenrollment.domain.model.Student;
import com.elliotgaramendi.studentenrollment.domain.port.out.StudentRepositoryPort;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.out.persistence.mapper.StudentPersistenceMapper;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.out.persistence.repository.StudentJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class StudentPersistenceAdapter implements StudentRepositoryPort {

    private final StudentJpaRepository studentJpaRepository;
    private final StudentPersistenceMapper studentPersistenceMapper;

    public StudentPersistenceAdapter(
            StudentJpaRepository studentJpaRepository,
            StudentPersistenceMapper studentPersistenceMapper
    ) {
        this.studentJpaRepository = studentJpaRepository;
        this.studentPersistenceMapper = studentPersistenceMapper;
    }

    @Override
    public Student save(Student student) {
        return studentPersistenceMapper.toDomain(
                studentJpaRepository.save(studentPersistenceMapper.toEntity(student))
        );
    }

    @Override
    public Optional<Student> findById(Long id) {
        return studentJpaRepository.findById(id).map(studentPersistenceMapper::toDomain);
    }

    @Override
    public List<Student> findAll() {
        return studentJpaRepository.findAll()
                .stream()
                .map(studentPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(Long id) {
        return studentJpaRepository.existsById(id);
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        return studentJpaRepository.findByEmail(email).map(studentPersistenceMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        studentJpaRepository.deleteById(id);
    }
}
