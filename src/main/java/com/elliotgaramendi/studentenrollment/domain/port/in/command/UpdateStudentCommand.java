package com.elliotgaramendi.studentenrollment.domain.port.in.command;

public record UpdateStudentCommand(
        String firstName,
        String lastName,
        String email
) {
}
