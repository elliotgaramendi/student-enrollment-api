package com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OpenApiDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void exposesSwaggerUi() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void exposesProfessionalOpenApiMetadata() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("Student Enrollment API"))
                .andExpect(jsonPath("$.info.version").value("v1.0.0"))
                .andExpect(jsonPath("$.info.description").value(org.hamcrest.Matchers.containsString("hexagonal architecture")))
                .andExpect(jsonPath("$.servers[0].url").value("http://localhost:8080"));
    }

    @Test
    void documentsMainApiPaths() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/v1/students']").exists())
                .andExpect(jsonPath("$.paths['/api/v1/students/{id}']").exists())
                .andExpect(jsonPath("$.paths['/api/v1/students/{id}/enrollments']").exists())
                .andExpect(jsonPath("$.paths['/api/v1/student-enrollments']").exists())
                .andExpect(jsonPath("$.paths['/api/v1/student-enrollments/{id}']").exists());
    }

    @Test
    void documentsRealHttpResponseCodes() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/v1/students'].post.responses['201']").exists())
                .andExpect(jsonPath("$.paths['/api/v1/students'].post.responses['409']").exists())
                .andExpect(jsonPath("$.paths['/api/v1/students/{id}'].delete.responses['204']").exists())
                .andExpect(jsonPath("$.paths['/api/v1/student-enrollments'].post.responses['201']").exists())
                .andExpect(jsonPath("$.paths['/api/v1/student-enrollments/{id}'].delete.responses['204']").exists());
    }

    @Test
    void documentsDomainTags() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/v1/students'].get.tags[0]").value("Students"))
                .andExpect(jsonPath("$.paths['/api/v1/student-enrollments'].get.tags[0]").value("Student Enrollments"))
                .andExpect(jsonPath("$.tags[*].name", hasItems("Students", "Student Enrollments")));
    }
}
