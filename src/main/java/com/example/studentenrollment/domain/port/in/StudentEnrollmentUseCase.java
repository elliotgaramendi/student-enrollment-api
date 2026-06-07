package com.example.studentenrollment.domain.port.in;

import com.example.studentenrollment.domain.model.StudentEnrollment;
import com.example.studentenrollment.domain.port.in.command.CreateStudentEnrollmentCommand;
import com.example.studentenrollment.domain.port.in.command.UpdateStudentEnrollmentCommand;

import java.util.List;

public interface StudentEnrollmentUseCase {

    StudentEnrollment createEnrollment(CreateStudentEnrollmentCommand command);

    StudentEnrollment getEnrollmentById(Long id);

    List<StudentEnrollment> listEnrollments();

    List<StudentEnrollment> listEnrollmentsByStudentId(Long studentId);

    StudentEnrollment updateEnrollment(Long id, UpdateStudentEnrollmentCommand command);

    void deleteEnrollment(Long id);
}
