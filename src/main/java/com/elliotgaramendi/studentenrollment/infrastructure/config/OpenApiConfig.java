package com.elliotgaramendi.studentenrollment.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI studentEnrollmentOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Student Enrollment API")
                        .version("v1.0.0")
                        .description("""
                                REST API for managing students and course enrollments.
                                Built with Spring Boot and a simple hexagonal architecture for an academic backend project.
                                """)
                        .contact(new Contact()
                                .name("Academic Project Team")))
                .servers(List.of(new Server()
                        .url("/")
                        .description("Current server")));
    }
}
