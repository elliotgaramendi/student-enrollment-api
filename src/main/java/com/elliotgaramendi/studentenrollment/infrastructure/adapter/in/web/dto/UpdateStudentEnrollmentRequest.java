package com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Payload used to update a student enrollment.")
public record UpdateStudentEnrollmentRequest(
        @Schema(description = "Course code assigned to the enrollment", example = "AI-ENGINEER", maxLength = 50)
        @NotBlank(message = "Course code is required")
        @Size(max = 50, message = "Course code must not exceed 50 characters")
        String courseCode,

        @Schema(description = "Enrollment date. Future dates are not allowed.", example = "2026-01-31")
        @NotNull(message = "Enrollment date is required")
        @PastOrPresent(message = "Enrollment date must not be in the future")
        LocalDate enrollmentDate
) {
}
