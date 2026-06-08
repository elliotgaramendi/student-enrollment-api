package com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.mapper;

import com.elliotgaramendi.studentenrollment.domain.model.StudentEnrollment;
import com.elliotgaramendi.studentenrollment.domain.port.in.command.CreateStudentEnrollmentCommand;
import com.elliotgaramendi.studentenrollment.domain.port.in.command.UpdateStudentEnrollmentCommand;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto.CreateStudentEnrollmentRequest;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto.StudentEnrollmentResponse;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto.UpdateStudentEnrollmentRequest;
import org.springframework.stereotype.Component;

@Component
public class StudentEnrollmentWebMapper {

    public CreateStudentEnrollmentCommand toCommand(CreateStudentEnrollmentRequest request) {
        return new CreateStudentEnrollmentCommand(
                request.studentId(),
                request.courseCode(),
                request.enrollmentDate()
        );
    }

    public UpdateStudentEnrollmentCommand toCommand(UpdateStudentEnrollmentRequest request) {
        return new UpdateStudentEnrollmentCommand(request.courseCode(), request.enrollmentDate());
    }

    public StudentEnrollmentResponse toResponse(StudentEnrollment enrollment) {
        return new StudentEnrollmentResponse(
                enrollment.getId(),
                enrollment.getStudentId(),
                enrollment.getCourseCode(),
                enrollment.getEnrollmentDate()
        );
    }
}
