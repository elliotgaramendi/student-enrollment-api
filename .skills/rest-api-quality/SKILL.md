---
name: rest-api-quality
description: Use when adding or reviewing REST controllers, DTOs, validation, error responses, and Swagger/OpenAPI behavior for student-enrollment-api.
---

# REST API Quality

Use this skill when implementing the web adapter.

## Rules

- Place REST code under `infrastructure/adapter/in/web`.
- Use request and response DTOs for HTTP I/O.
- Use Jakarta Validation on request DTOs, not domain models.
- Controllers call input ports only.
- Return clear HTTP status codes: 201 for create, 200 for read/update, 204 for delete, 404 for missing resources, 400 for validation errors.
- Keep response field names in English and stable.
- Document endpoints through Springdoc/OpenAPI.

## Checks

- Add MockMvc tests for successful and failing requests.
- Confirm Swagger UI remains available at `/swagger-ui/index.html`.
