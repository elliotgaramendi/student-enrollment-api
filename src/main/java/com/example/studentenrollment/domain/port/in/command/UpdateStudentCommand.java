package com.example.studentenrollment.domain.port.in.command;

public record UpdateStudentCommand(
        String firstName,
        String lastName,
        String email
) {
}
