package com.example.studentenrollment.domain.port.in.command;

public record CreateStudentCommand(
        String firstName,
        String lastName,
        String email
) {
}
