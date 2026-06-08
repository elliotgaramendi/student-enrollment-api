---
name: spring-boot-hexagonal
description: Use when developing this student-enrollment-api project with Spring Boot and hexagonal architecture, especially domain, application services, ports, and infrastructure adapters.
---

# Spring Boot Hexagonal

Use this skill for this project whenever adding backend code.

## Rules

- Keep `domain` framework-free: no Spring, JPA, Hibernate, Jakarta Validation, DTOs, or persistence annotations.
- Put use case implementations in `application/service`.
- Application services implement input ports and depend only on output ports.
- Put JPA, Spring Data, Docker, and external integrations under `infrastructure`.
- Controllers must call input ports, never repositories or adapters directly.
- Persistence adapters implement output ports and map between domain models and JPA entities.

## Checks

- Run `mvn test` after changes.
- Search `domain` for forbidden imports before finishing:
  `org.springframework|jakarta.persistence|jakarta.validation|org.hibernate`
