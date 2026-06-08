package com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsStudent() throws Exception {
        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Elliot",
                                  "lastName": "Garamendi",
                                  "email": "elliot.create@example.com"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/api/v1/students/")))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.firstName").value("Elliot"))
                .andExpect(jsonPath("$.lastName").value("Garamendi"))
                .andExpect(jsonPath("$.email").value("elliot.create@example.com"));
    }

    @Test
    void listsStudents() throws Exception {
        createStudent("Ada", "Lovelace", "ada.list@example.com");
        createStudent("Grace", "Hopper", "grace.list@example.com");

        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getsStudentById() throws Exception {
        Long studentId = createStudent("Ada", "Lovelace", "ada.get@example.com");

        mockMvc.perform(get("/api/v1/students/{id}", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(studentId))
                .andExpect(jsonPath("$.email").value("ada.get@example.com"));
    }

    @Test
    void updatesStudent() throws Exception {
        Long studentId = createStudent("Ada", "Lovelace", "ada.update@example.com");

        mockMvc.perform(put("/api/v1/students/{id}", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Ada",
                                  "lastName": "Byron",
                                  "email": "ada.updated@example.com"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(studentId))
                .andExpect(jsonPath("$.lastName").value("Byron"))
                .andExpect(jsonPath("$.email").value("ada.updated@example.com"));
    }

    @Test
    void deletesStudent() throws Exception {
        Long studentId = createStudent("Ada", "Lovelace", "ada.delete@example.com");

        mockMvc.perform(delete("/api/v1/students/{id}", studentId))
                .andExpect(status().isNoContent());
    }

    @Test
    void returnsNotFoundForMissingStudent() throws Exception {
        mockMvc.perform(get("/api/v1/students/{id}", 999999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Resource Not Found"))
                .andExpect(jsonPath("$.detail").value("Student not found with id: 999999"));
    }

    @Test
    void returnsBadRequestForInvalidStudentRequest() throws Exception {
        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "",
                                  "lastName": "Garamendi",
                                  "email": "not-an-email"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation Error"))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void returnsConflictForDuplicateEmail() throws Exception {
        createStudent("Elliot", "Garamendi", "duplicate@example.com");

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Other",
                                  "lastName": "Student",
                                  "email": "duplicate@example.com"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Duplicate Resource"))
                .andExpect(jsonPath("$.detail").value("Student email already exists: duplicate@example.com"));
    }

    private Long createStudent(String firstName, String lastName, String email) throws Exception {
        String response = mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "%s",
                                  "lastName": "%s",
                                  "email": "%s"
                                }
                                """.formatted(firstName, lastName, email)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return Long.valueOf(response.replaceAll(".*\\\"id\\\":(\\d+).*", "$1"));
    }
}
