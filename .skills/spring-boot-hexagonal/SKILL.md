---
name: spring-boot-hexagonal
description: Use when developing student-enrollment-api with Java 17, Spring Boot 3.5, Maven, and simple hexagonal architecture. Applies to domain models, ports, application services, REST adapters, persistence adapters, and architecture checks.
---

# Spring Boot Hexagonal

Use this skill for this project whenever adding backend code.

## Architecture Rules

- Keep `domain` framework-free: no Spring, JPA, Hibernate, Jakarta Validation, DTOs, persistence annotations, or Lombok.
- Put pure business models in `domain/model`.
- Put input and output ports in `domain/port`.
- Put use case implementations in `application/service`.
- Application services implement input ports and depend on output ports only.
- Put REST, JPA, Spring Data, Docker, OpenAPI, and external integrations under `infrastructure`.
- Controllers must call input ports, never repositories or adapters directly.
- Persistence adapters implement output ports and map between domain models and JPA entities.
- Use explicit constructors/getters/setters or records where already established; do not introduce Lombok.

## Checks

- Run `mvn test` after changes.
- Search `domain` for forbidden imports before finishing:
  `org.springframework|jakarta.persistence|jakarta.validation|org.hibernate`
- Keep package base `com.elliotgaramendi.studentenrollment`.
