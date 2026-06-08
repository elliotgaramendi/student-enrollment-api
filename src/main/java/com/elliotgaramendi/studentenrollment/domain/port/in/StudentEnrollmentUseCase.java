package com.elliotgaramendi.studentenrollment.domain.port.in;

import com.elliotgaramendi.studentenrollment.domain.model.StudentEnrollment;
import com.elliotgaramendi.studentenrollment.domain.port.in.command.CreateStudentEnrollmentCommand;
import com.elliotgaramendi.studentenrollment.domain.port.in.command.UpdateStudentEnrollmentCommand;

import java.util.List;

public interface StudentEnrollmentUseCase {

    StudentEnrollment createEnrollment(CreateStudentEnrollmentCommand command);

    StudentEnrollment getEnrollmentById(Long id);

    List<StudentEnrollment> listEnrollments();

    List<StudentEnrollment> listEnrollmentsByStudentId(Long studentId);

    StudentEnrollment updateEnrollment(Long id, UpdateStudentEnrollmentCommand command);

    void deleteEnrollment(Long id);
}
