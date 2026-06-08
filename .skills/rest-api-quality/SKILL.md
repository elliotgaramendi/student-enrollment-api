---
name: rest-api-quality
description: Use when adding or reviewing REST API behavior in student-enrollment-api, including controllers, DTOs, Jakarta Validation, ProblemDetail errors, MockMvc tests, and Springdoc OpenAPI documentation.
---

# REST API Quality

Use this skill when implementing the web adapter.

## REST Rules

- Place REST code under `infrastructure/adapter/in/web`.
- Use request and response DTOs for HTTP I/O.
- Use Jakarta Validation on request DTOs, not domain models.
- Controllers call input ports only.
- Map request DTOs to commands and domain models through web mappers.
- Return stable English response fields.
- Keep API routes under `/api/v1`.

## Status Codes

- `201 Created` for create.
- `200 OK` for read, list, and update.
- `204 No Content` for delete.
- `400 Bad Request` for validation, malformed JSON, invalid path values, and invalid query values.
- `404 Not Found` for missing students or enrollments.
- `409 Conflict` for duplicate student email.
- `500 Internal Server Error` for unexpected failures.

## Error And Documentation Rules

- Use Spring `ProblemDetail` for error responses.
- Include an `errors` property for validation and handled errors.
- Keep Swagger UI available at `/swagger-ui/index.html`.
- Keep OpenAPI JSON available at `/v3/api-docs`.
- Document endpoints with tags `Students` and `Student Enrollments`.
- Document DTO examples with `@Schema`.

## Endpoints

- Students: `/api/v1/students`.
- Nested student enrollments: `/api/v1/students/{id}/enrollments`.
- Student enrollments: `/api/v1/student-enrollments`.
- Enrollment filter: `/api/v1/student-enrollments?studentId=1`.

## Checks

- Add or update MockMvc tests for successful and failing requests.
- Confirm validation errors, missing resources, duplicate email, malformed JSON, and invalid path/query values.
- Run `mvn test`.
- Keep response field names in English and stable.
- Document endpoints through Springdoc/OpenAPI.

## Checks

- Add MockMvc tests for successful and failing requests.
- Confirm Swagger UI remains available at `/swagger-ui/index.html`.
