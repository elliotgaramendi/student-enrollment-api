package com.elliotgaramendi.studentenrollment.application.service;

import com.elliotgaramendi.studentenrollment.application.exception.DuplicateResourceException;
import com.elliotgaramendi.studentenrollment.application.exception.ResourceNotFoundException;
import com.elliotgaramendi.studentenrollment.domain.model.Student;
import com.elliotgaramendi.studentenrollment.domain.port.in.StudentUseCase;
import com.elliotgaramendi.studentenrollment.domain.port.in.command.CreateStudentCommand;
import com.elliotgaramendi.studentenrollment.domain.port.in.command.UpdateStudentCommand;
import com.elliotgaramendi.studentenrollment.domain.port.out.StudentEnrollmentRepositoryPort;
import com.elliotgaramendi.studentenrollment.domain.port.out.StudentRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService implements StudentUseCase {

    private final StudentRepositoryPort studentRepositoryPort;
    private final StudentEnrollmentRepositoryPort studentEnrollmentRepositoryPort;

    public StudentService(
            StudentRepositoryPort studentRepositoryPort,
            StudentEnrollmentRepositoryPort studentEnrollmentRepositoryPort
    ) {
        this.studentRepositoryPort = studentRepositoryPort;
        this.studentEnrollmentRepositoryPort = studentEnrollmentRepositoryPort;
    }

    @Override
    public Student createStudent(CreateStudentCommand command) {
        ensureEmailIsAvailable(command.email(), null);
        Student student = new Student(null, command.firstName(), command.lastName(), command.email());
        return studentRepositoryPort.save(student);
    }

    @Override
    public Student getStudentById(Long id) {
        return findStudentOrThrow(id);
    }

    @Override
    public List<Student> listStudents() {
        return studentRepositoryPort.findAll();
    }

    @Override
    public Student updateStudent(Long id, UpdateStudentCommand command) {
        Student student = findStudentOrThrow(id);
        ensureEmailIsAvailable(command.email(), id);
        student.updateProfile(command.firstName(), command.lastName(), command.email());
        return studentRepositoryPort.save(student);
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepositoryPort.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }

        studentEnrollmentRepositoryPort.deleteByStudentId(id);
        studentRepositoryPort.deleteById(id);
    }

    private Student findStudentOrThrow(Long id) {
        return studentRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    private void ensureEmailIsAvailable(String email, Long currentStudentId) {
        studentRepositoryPort.findByEmail(email)
                .filter(existingStudent -> !existingStudent.getId().equals(currentStudentId))
                .ifPresent(existingStudent -> {
                    throw new DuplicateResourceException("Student email already exists: " + email);
                });
    }
}
