package com.example.studentenrollment.domain.port.in.command;

import java.time.LocalDate;

public record UpdateStudentEnrollmentCommand(
        String courseCode,
        LocalDate enrollmentDate
) {
}
