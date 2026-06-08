package com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.mapper;

import com.elliotgaramendi.studentenrollment.domain.model.Student;
import com.elliotgaramendi.studentenrollment.domain.port.in.command.CreateStudentCommand;
import com.elliotgaramendi.studentenrollment.domain.port.in.command.UpdateStudentCommand;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto.CreateStudentRequest;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto.StudentResponse;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto.UpdateStudentRequest;
import org.springframework.stereotype.Component;

@Component
public class StudentWebMapper {

    public CreateStudentCommand toCommand(CreateStudentRequest request) {
        return new CreateStudentCommand(request.firstName(), request.lastName(), request.email());
    }

    public UpdateStudentCommand toCommand(UpdateStudentRequest request) {
        return new UpdateStudentCommand(request.firstName(), request.lastName(), request.email());
    }

    public StudentResponse toResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail()
        );
    }
}
