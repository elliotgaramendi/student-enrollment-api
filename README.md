# student-enrollment-api

Student enrollment REST API built with Java 17, Spring Boot 3, Maven, Spring Web, Spring Data JPA, MySQL 8, Jakarta Validation, Springdoc OpenAPI, JUnit 5, MockMvc, and H2 for automated tests.

## Quick Start

Run the production-like backend and MySQL stack with Docker:

```bash
cp .env.example .env
docker compose --env-file .env up -d --build
```

Then open Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

Quick API smoke test:

```bash
curl -i -X POST http://localhost:8080/api/v1/students \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Elliot",
    "lastName": "Garamendi",
    "email": "elliot@example.com"
  }'
```

```bash
curl http://localhost:8080/api/v1/students
```

Stop the containers:

```bash
docker compose --env-file .env down
```

If `.env` is missing, create it again with:

```bash
cp .env.example .env
```

The project follows a simple hexagonal architecture:

- `domain`: business models and ports without Spring or JPA annotations.
- `application`: use case services and application exceptions.
- `infrastructure.adapter.in.web`: REST controllers, DTOs, validation, and web mappers.
- `infrastructure.adapter.out.persistence`: JPA entities, repositories, adapters, and persistence mappers.
- `infrastructure.exception`: global REST exception handling.

## Requirements

- Docker and Docker Compose
- Java 17 and Maven 3.9+ only if you want to run the app outside Docker

You do not need a local MySQL installation. Docker Compose provides MySQL for both development and production-like runs.

## Environment Variables

Copy the example file before running the application with Docker Compose:

```bash
cp .env.example .env
```

The local `.env` file is ignored by Git. The versioned `.env.example` contains safe academic defaults:

```text
APP_PORT=8080
MYSQL_PORT=3306
MYSQL_DATABASE=student_enrollment_db
MYSQL_USER=CHANGE_ME_DB_USER
MYSQL_PASSWORD=CHANGE_ME_DB_PASSWORD
MYSQL_ROOT_PASSWORD=CHANGE_ME_ROOT_PASSWORD
DB_URL=jdbc:mysql://mysql:3306/student_enrollment_db
DB_USERNAME=CHANGE_ME_DB_USER
DB_PASSWORD=CHANGE_ME_DB_PASSWORD
JPA_DDL_AUTO=update
```

`application.yml` also provides defaults, so `mvn spring-boot:run` can run against a Docker MySQL database through `localhost`.

## Commands

Run automated tests with H2:

```bash
mvn test
```

Run the same test suite without installing Maven locally:

```bash
docker run --rm \
  -v "$PWD":/workspace \
  -v student-enrollment-m2:/root/.m2 \
  -w /workspace \
  maven:3.9.11-eclipse-temurin-17 \
  mvn test
```

Start only MySQL with Docker Compose:

```bash
docker compose up -d mysql
```

Run the application locally against Docker MySQL:

```bash
mvn spring-boot:run
```

Run the application and MySQL fully containerized in production-like mode:

```bash
docker compose --env-file .env up -d --build
```

Run the application in Docker development mode with source watch and automatic app restart:

```bash
docker compose --env-file .env -f docker-compose.yml -f docker-compose.dev.yml watch
```

Java does not behave like Vite hot module replacement. In this project, Docker development mode syncs source changes into the container and restarts the Spring Boot process, which recompiles the app without rebuilding the full production image.

Follow app logs:

```bash
docker compose --env-file .env logs -f app
```

Stop containers while keeping the MySQL data volume:

```bash
docker compose --env-file .env down
```

Stop containers and delete the MySQL data volume:

```bash
docker compose --env-file .env down -v
```

Validate the Compose configuration:

```bash
docker compose --env-file .env.example config
```

## Automated Tests

Automated tests run with JUnit 5, MockMvc, Spring Boot Test, Mockito, AssertJ, Spring Data JPA test slices, and H2 in MySQL compatibility mode.

The test profile uses an in-memory H2 database to keep CRUD and integration tests fast:

```text
jdbc:h2:mem:student_enrollment_test;MODE=MySQL
```

The suite covers:

- application services and business rules;
- persistence adapters and JPA mappings;
- REST API flows for students and enrollments;
- validation and `ProblemDetail` error responses;
- OpenAPI/Swagger documentation endpoints;
- Spring application context startup.

JaCoCo generates a coverage report every time tests run:

```text
target/site/jacoco/index.html
target/site/jacoco/jacoco.xml
```

Docker/MySQL is still used for runtime integration validation: container startup, environment variables, MySQL connectivity, healthcheck, Swagger, and real HTTP smoke tests. H2 keeps automated CRUD tests lightweight; Docker/MySQL confirms the packaged application works in its deployment-like environment.

## Demo Data

The project includes API-based scripts to reset and seed demo data. Keep the backend running first:

```bash
docker compose --env-file .env up -d --build
```

Clear all students and enrollments:

```bash
bash scripts/reset-data.sh
```

Load 8 demo students and 8 enrollments:

```bash
bash scripts/seed-data.sh
```

Verify students:

```bash
curl http://localhost:8080/api/v1/students
```

Verify enrollments:

```bash
curl http://localhost:8080/api/v1/student-enrollments
```

Use a different backend URL without editing scripts:

```bash
BASE_URL=http://localhost:8080 bash scripts/seed-data.sh
```

## Swagger UI

When the application is running, Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

## Health Check

Docker uses the Spring Boot Actuator health endpoint to check whether the backend is ready:

```text
http://localhost:8080/actuator/health
```

This endpoint is technical infrastructure and is not part of the academic CRUD API.

## REST API

Base path:

```text
/api/v1
```

### Students

```text
POST   /api/v1/students
GET    /api/v1/students
GET    /api/v1/students/{id}
PUT    /api/v1/students/{id}
DELETE /api/v1/students/{id}
GET    /api/v1/students/{id}/enrollments
```

Create a student:

```bash
curl -i -X POST http://localhost:8080/api/v1/students \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Elliot",
    "lastName": "Garamendi",
    "email": "elliot@example.com"
  }'
```

### Student Enrollments

```text
POST   /api/v1/student-enrollments
GET    /api/v1/student-enrollments
GET    /api/v1/student-enrollments?studentId=1
GET    /api/v1/student-enrollments/{id}
PUT    /api/v1/student-enrollments/{id}
DELETE /api/v1/student-enrollments/{id}
```

Create an enrollment:

```bash
curl -i -X POST http://localhost:8080/api/v1/student-enrollments \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": 1,
    "courseCode": "MATH-101",
    "enrollmentDate": "2026-06-07"
  }'
```

## Validation And Errors

Request DTOs use Jakarta Validation:

- Student names are required and limited to 100 characters.
- Student email is required, valid, unique, and limited to 150 characters.
- Enrollment `studentId` is required and positive.
- Enrollment `courseCode` is required and limited to 50 characters.
- Enrollment date is required and must not be in the future.

Errors use Spring `ProblemDetail`:

- `400 Bad Request` for validation, malformed JSON, and invalid path/query values.
- `404 Not Found` for missing students or enrollments.
- `409 Conflict` for duplicated student emails.
- `500 Internal Server Error` for unexpected failures.

## Local Skills

This project includes local AI-agent skills:

- `.agents/skills/spring-boot-skill`: Spring Boot implementation guidance.
- `.skills/spring-boot-hexagonal`: project-specific hexagonal architecture rules.
- `.skills/rest-api-quality`: project-specific REST API quality rules.
- `.skills/docker-mysql`: project-specific Docker/MySQL workflow rules.
