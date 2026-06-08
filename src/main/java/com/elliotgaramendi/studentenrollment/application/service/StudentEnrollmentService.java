package com.elliotgaramendi.studentenrollment.application.service;

import com.elliotgaramendi.studentenrollment.application.exception.ResourceNotFoundException;
import com.elliotgaramendi.studentenrollment.domain.model.StudentEnrollment;
import com.elliotgaramendi.studentenrollment.domain.port.in.StudentEnrollmentUseCase;
import com.elliotgaramendi.studentenrollment.domain.port.in.command.CreateStudentEnrollmentCommand;
import com.elliotgaramendi.studentenrollment.domain.port.in.command.UpdateStudentEnrollmentCommand;
import com.elliotgaramendi.studentenrollment.domain.port.out.StudentEnrollmentRepositoryPort;
import com.elliotgaramendi.studentenrollment.domain.port.out.StudentRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentEnrollmentService implements StudentEnrollmentUseCase {

    private final StudentEnrollmentRepositoryPort studentEnrollmentRepositoryPort;
    private final StudentRepositoryPort studentRepositoryPort;

    public StudentEnrollmentService(
            StudentEnrollmentRepositoryPort studentEnrollmentRepositoryPort,
            StudentRepositoryPort studentRepositoryPort
    ) {
        this.studentEnrollmentRepositoryPort = studentEnrollmentRepositoryPort;
        this.studentRepositoryPort = studentRepositoryPort;
    }

    @Override
    public StudentEnrollment createEnrollment(CreateStudentEnrollmentCommand command) {
        ensureStudentExists(command.studentId());

        StudentEnrollment enrollment = new StudentEnrollment(
                null,
                command.studentId(),
                command.courseCode(),
                command.enrollmentDate()
        );

        return studentEnrollmentRepositoryPort.save(enrollment);
    }

    @Override
    public StudentEnrollment getEnrollmentById(Long id) {
        return findEnrollmentOrThrow(id);
    }

    @Override
    public List<StudentEnrollment> listEnrollments() {
        return studentEnrollmentRepositoryPort.findAll();
    }

    @Override
    public List<StudentEnrollment> listEnrollmentsByStudentId(Long studentId) {
        ensureStudentExists(studentId);
        return studentEnrollmentRepositoryPort.findByStudentId(studentId);
    }

    @Override
    public StudentEnrollment updateEnrollment(Long id, UpdateStudentEnrollmentCommand command) {
        StudentEnrollment enrollment = findEnrollmentOrThrow(id);
        enrollment.updateEnrollment(command.courseCode(), command.enrollmentDate());
        return studentEnrollmentRepositoryPort.save(enrollment);
    }

    @Override
    public void deleteEnrollment(Long id) {
        if (studentEnrollmentRepositoryPort.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Student enrollment not found with id: " + id);
        }

        studentEnrollmentRepositoryPort.deleteById(id);
    }

    private void ensureStudentExists(Long studentId) {
        if (!studentRepositoryPort.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with id: " + studentId);
        }
    }

    private StudentEnrollment findEnrollmentOrThrow(Long id) {
        return studentEnrollmentRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student enrollment not found with id: " + id));
    }
}
