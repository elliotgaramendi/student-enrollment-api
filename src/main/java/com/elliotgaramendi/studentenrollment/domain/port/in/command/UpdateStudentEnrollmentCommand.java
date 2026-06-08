package com.elliotgaramendi.studentenrollment.domain.port.in.command;

import java.time.LocalDate;

public record UpdateStudentEnrollmentCommand(
        String courseCode,
        LocalDate enrollmentDate
) {
}
