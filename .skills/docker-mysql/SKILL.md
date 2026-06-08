---
name: docker-mysql
description: Use when configuring Docker, Docker Compose, MySQL 8, application database environment variables, or local runtime commands for student-enrollment-api.
---

# Docker MySQL

Use this skill for local runtime and containerized database work.

## Rules

- Do not require MySQL installed on the host machine.
- Use Docker Compose service name `mysql` from containers.
- Use `localhost` only when the Spring Boot app runs directly on the host machine.
- Keep tests on H2 unless the task explicitly requires Docker/MySQL integration.
- Keep credentials consistent with `docker-compose.yml`.

## Commands

```bash
docker compose up -d mysql
mvn spring-boot:run
docker compose up --build
mvn test
```

## Connection URLs

- Host app to Docker MySQL: `jdbc:mysql://localhost:3306/student_enrollment_db`
- Docker app to Docker MySQL: `jdbc:mysql://mysql:3306/student_enrollment_db`
