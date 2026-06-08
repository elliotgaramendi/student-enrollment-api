# student-enrollment-api

Student enrollment REST API built with Java 17, Spring Boot 3, Maven, Spring Web, Spring Data JPA, MySQL, Jakarta Validation, Springdoc OpenAPI, JUnit 5, MockMvc, and H2 for automated tests.

This first stage only provides the executable Spring Boot base. Business logic, REST endpoints, persistence adapters, Docker, and custom OpenAPI metadata will be added in later stages.

## Requirements

- Java 17
- Maven 3.9+

## Commands

```bash
mvn test
mvn spring-boot:run
```

Tests use H2 in memory, so they do not require Docker or MySQL:

```bash
mvn test
```

## MySQL With Docker

You do not need to install MySQL locally. Start MySQL with Docker Compose:

```bash
docker compose up -d mysql
```

Then run the Spring Boot application locally:

```bash
mvn spring-boot:run
```

When the app runs on your machine, it connects to Docker MySQL through `localhost`:

```text
jdbc:mysql://localhost:3306/student_enrollment_db
```

To run both the application and MySQL inside Docker:

```bash
docker compose up --build
```

When the app runs inside Docker Compose, it connects to MySQL through the service name `mysql`:

```text
jdbc:mysql://mysql:3306/student_enrollment_db
```

The default runtime configuration is prepared through environment variables:

```text
DB_URL=jdbc:mysql://localhost:3306/student_enrollment_db
DB_USERNAME=student_user
DB_PASSWORD=student_password
JPA_DDL_AUTO=update
```

## Swagger UI

When the application is running, Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

At this stage there are no business endpoints yet.

## Local Skills

This project includes local AI-agent skills:

- `.agents/skills/spring-boot-skill`: external Spring Boot skill from the `skills.sh` ecosystem.
- `.skills/spring-boot-hexagonal`: project-specific hexagonal architecture rules.
- `.skills/rest-api-quality`: project-specific REST API quality rules.
- `.skills/docker-mysql`: project-specific Docker/MySQL workflow rules.
