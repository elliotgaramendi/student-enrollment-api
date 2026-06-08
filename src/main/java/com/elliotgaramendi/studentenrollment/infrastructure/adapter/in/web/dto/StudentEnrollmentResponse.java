package com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto;

import java.time.LocalDate;

public record StudentEnrollmentResponse(
        Long id,
        Long studentId,
        String courseCode,
        LocalDate enrollmentDate
) {
}
