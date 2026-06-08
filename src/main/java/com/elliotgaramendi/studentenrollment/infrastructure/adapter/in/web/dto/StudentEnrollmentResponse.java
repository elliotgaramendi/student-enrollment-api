package com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Student enrollment returned by the API.")
public record StudentEnrollmentResponse(
        @Schema(description = "Generated enrollment id", example = "1")
        Long id,

        @Schema(description = "Generated student id related to the enrollment", example = "1")
        Long studentId,

        @Schema(description = "Course code assigned to the enrollment", example = "AI-ENGINEER")
        String courseCode,

        @Schema(description = "Enrollment date", example = "2026-01-31")
        LocalDate enrollmentDate
) {
}
