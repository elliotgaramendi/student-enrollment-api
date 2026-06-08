package com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateStudentEnrollmentRequest(
        @NotNull(message = "Student id is required")
        @Positive(message = "Student id must be positive")
        Long studentId,

        @NotBlank(message = "Course code is required")
        @Size(max = 50, message = "Course code must not exceed 50 characters")
        String courseCode,

        @NotNull(message = "Enrollment date is required")
        @PastOrPresent(message = "Enrollment date must not be in the future")
        LocalDate enrollmentDate
) {
}
