package com.elliotgaramendi.studentenrollment.domain.port.in;

import com.elliotgaramendi.studentenrollment.domain.model.Student;
import com.elliotgaramendi.studentenrollment.domain.port.in.command.CreateStudentCommand;
import com.elliotgaramendi.studentenrollment.domain.port.in.command.UpdateStudentCommand;

import java.util.List;

public interface StudentUseCase {

    Student createStudent(CreateStudentCommand command);

    Student getStudentById(Long id);

    List<Student> listStudents();

    Student updateStudent(Long id, UpdateStudentCommand command);

    void deleteStudent(Long id);
}
