package com.elliotgaramendi.studentenrollment.domain.port.in.command;

public record CreateStudentCommand(
        String firstName,
        String lastName,
        String email
) {
}
