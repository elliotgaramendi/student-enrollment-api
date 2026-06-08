# student-enrollment-api

Student enrollment REST API built with Java 17, Spring Boot 3.5, Maven, Spring Web, Spring Data JPA, MySQL 8, Docker, Springdoc OpenAPI, Jakarta Validation, JUnit 5, MockMvc, H2, and JaCoCo.

The project uses a simple hexagonal architecture: the domain stays framework-free, application services implement use cases, REST controllers expose HTTP endpoints, and persistence adapters connect the application to MySQL through JPA.

## Tech Stack

- Java 17
- Spring Boot 3.5.x
- Maven
- Spring Web
- Spring Data JPA
- MySQL 8
- Docker and Docker Compose
- Springdoc OpenAPI / Swagger UI
- Jakarta Validation
- JUnit 5, MockMvc, H2
- JaCoCo
- No Lombok

## Quick Start

Run the complete backend and MySQL stack with Docker:

```bash
cp .env.example .env
docker compose --env-file .env up -d --build
```

Open Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

Smoke test:

```bash
curl http://localhost:8080/actuator/health
curl http://localhost:8080/api/v1/students
```

Stop the stack:

```bash
docker compose --env-file .env down
```

You do not need MySQL installed locally. Docker Compose provides MySQL.

## Environment

Create a local `.env` file from the safe example:

```bash
cp .env.example .env
```

`.env` is ignored by Git. Keep real credentials only there.

```env
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

Inside Docker, the backend connects to MySQL using hostname `mysql`. If you run Spring Boot directly on your machine, use `localhost` for the database URL.

`APP_PORT` controls the public host port used by Docker Compose. In a VPS where `8080` is already busy, use another port:

```env
APP_PORT=8084
MYSQL_PORT=3307
```

## Development With Docker

Run the app in Docker development mode with source sync and automatic restart:

```bash
docker compose --env-file .env -f docker-compose.yml -f docker-compose.dev.yml watch
```

Java does not use frontend-style hot module replacement like Vite. This setup syncs source changes into the container and restarts Spring Boot automatically.

Follow logs:

```bash
docker compose --env-file .env logs -f app
```

Delete containers and the MySQL data volume:

```bash
docker compose --env-file .env down -v
```

## Architecture

```text
src/main/java/com/elliotgaramendi/studentenrollment
├── domain
│   ├── model
│   └── port
├── application
│   ├── exception
│   └── service
└── infrastructure
    ├── adapter/in/web
    ├── adapter/out/persistence
    ├── config
    └── exception
```

Rules:

- Controllers call input ports, not repositories.
- Application services depend on output ports.
- Persistence adapters implement output ports.
- DTOs are used only for HTTP input/output.
- Domain models do not use Spring, JPA, validation, or framework annotations.

## REST API

Base path:

```text
/api/v1
```

Students:

```text
POST   /students
GET    /students
GET    /students/{id}
PUT    /students/{id}
DELETE /students/{id}
GET    /students/{id}/enrollments
```

Student enrollments:

```text
POST   /student-enrollments
GET    /student-enrollments
GET    /student-enrollments?studentId=1
GET    /student-enrollments/{id}
PUT    /student-enrollments/{id}
DELETE /student-enrollments/{id}
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

Create an enrollment:

```bash
curl -i -X POST http://localhost:8080/api/v1/student-enrollments \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": 1,
    "courseCode": "AI-ENGINEER",
    "enrollmentDate": "2026-01-31"
  }'
```

## Swagger And Health

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

On a VPS using another `APP_PORT`, open Swagger with that port:

```text
http://TU_IP_DEL_VPS:8084/swagger-ui/index.html
```

Swagger UI uses the same host and port from the browser URL, so requests execute against the current server instead of a hardcoded `localhost`.

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

Health check:

```text
http://localhost:8080/actuator/health
```

## Validation And Errors

Request validation uses Jakarta Validation:

- student names: required, max 100 characters;
- email: required, valid, unique, max 150 characters;
- `studentId`: required and positive;
- `courseCode`: required, max 50 characters;
- `enrollmentDate`: required and not future.

Errors use Spring `ProblemDetail`:

- `400 Bad Request` for validation, malformed JSON, and invalid path/query values;
- `404 Not Found` for missing students or enrollments;
- `409 Conflict` for duplicate emails;
- `500 Internal Server Error` for unexpected failures.

## Demo Data

Keep the backend running, then reset and seed demo data through the API:

```bash
bash scripts/reset-data.sh
bash scripts/seed-data.sh
```

Verify:

```bash
curl http://localhost:8080/api/v1/students
curl http://localhost:8080/api/v1/student-enrollments
```

Use another backend URL if needed:

```bash
BASE_URL=http://localhost:8080 bash scripts/seed-data.sh
```

## Automated Tests

Run tests locally:

```bash
mvn test
```

Run tests without installing Maven:

```bash
docker run --rm \
  -v "$PWD":/workspace \
  -v student-enrollment-m2:/root/.m2 \
  -w /workspace \
  maven:3.9.11-eclipse-temurin-17 \
  mvn test
```

Tests use H2 in MySQL compatibility mode:

```text
jdbc:h2:mem:student_enrollment_test;MODE=MySQL
```

The suite covers service rules, persistence adapters, REST flows, validation errors, OpenAPI documentation, and Spring context startup.

JaCoCo report:

```text
target/site/jacoco/index.html
target/site/jacoco/jacoco.xml
```

Docker/MySQL validates runtime integration: container startup, environment variables, MySQL connectivity, healthcheck, Swagger, and real HTTP smoke tests. H2 keeps automated CRUD tests fast.

## Useful Commands

```bash
docker compose --env-file .env.example config
docker compose --env-file .env.example -f docker-compose.yml -f docker-compose.dev.yml config
docker compose --env-file .env up -d --build
docker compose --env-file .env logs -f app
docker compose --env-file .env down
docker compose --env-file .env down -v
```

## Local AI Skills

This repository includes local AI-agent instructions:

- `.skills/spring-boot-hexagonal`: project architecture rules.
- `.skills/rest-api-quality`: REST, validation, errors, and OpenAPI rules.
- `.skills/docker-mysql`: Docker, MySQL, `.env`, and runtime workflow rules.
- `.agents/skills/spring-boot-skill`: general Spring Boot reference skill.
