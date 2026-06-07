package com.example.studentenrollment.domain.port.in.command;

import java.time.LocalDate;

public record CreateStudentEnrollmentCommand(
        Long studentId,
        String courseCode,
        LocalDate enrollmentDate
) {
}
