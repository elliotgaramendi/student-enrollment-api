package com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Student returned by the API.")
public record StudentResponse(
        @Schema(description = "Generated student id", example = "1")
        Long id,

        @Schema(description = "Student first name", example = "Elliot")
        String firstName,

        @Schema(description = "Student last name", example = "Garamendi")
        String lastName,

        @Schema(description = "Unique student email address", example = "elliotgaramendi@gmail.com")
        String email
) {
}
