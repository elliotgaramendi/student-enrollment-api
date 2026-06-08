package com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto;

public record StudentResponse(
        Long id,
        String firstName,
        String lastName,
        String email
) {
}
