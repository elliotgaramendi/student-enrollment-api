---
name: docker-mysql
description: Use when configuring Docker, Docker Compose, MySQL 8, .env files, healthchecks, Docker development watch, or runtime validation for student-enrollment-api.
---

# Docker MySQL

Use this skill for local runtime and containerized database work.

## Rules

- Do not require MySQL installed on the host machine.
- Keep real credentials only in `.env`, which must stay ignored by Git.
- Keep `.env.example` safe with placeholders.
- Use Docker Compose service name `mysql` from containers.
- Use `localhost` only when Spring Boot runs directly on the host.
- Keep automated tests on H2 unless the task explicitly requires Docker/MySQL integration.
- Use Docker/MySQL for runtime validation: startup, env vars, connectivity, healthcheck, Swagger, and smoke tests.
- Keep app exposed on `8080` and MySQL exposed on `3306` unless the user explicitly changes `.env`.

## Commands

```bash
docker compose --env-file .env up -d --build
docker compose --env-file .env -f docker-compose.yml -f docker-compose.dev.yml watch
docker compose --env-file .env logs -f app
docker compose --env-file .env down
docker compose --env-file .env down -v
docker compose --env-file .env.example config
```

## Connection URLs

- Host app to Docker MySQL: `jdbc:mysql://localhost:3306/student_enrollment_db`
- Docker app to Docker MySQL: `jdbc:mysql://mysql:3306/student_enrollment_db`

## Checks

```bash
curl http://localhost:8080/actuator/health
curl http://localhost:8080/v3/api-docs
curl http://localhost:8080/api/v1/students
```

Do not commit real database usernames or passwords.
