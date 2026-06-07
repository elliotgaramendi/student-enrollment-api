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

The default runtime configuration is prepared for MySQL through environment variables:

```text
DB_URL=jdbc:mysql://localhost:3306/student_enrollment_db
DB_USERNAME=student_user
DB_PASSWORD=student_password
JPA_DDL_AUTO=none
```

Docker and the final MySQL runtime configuration will be added in a later stage.

## Swagger UI

When the application is running, Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

At this stage there are no business endpoints yet.
