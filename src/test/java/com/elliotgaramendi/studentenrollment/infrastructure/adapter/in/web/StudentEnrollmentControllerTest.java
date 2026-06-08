package com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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
class StudentEnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsEnrollment() throws Exception {
        Long studentId = createStudent("Elliot", "Garamendi", "elliot.enrollment.create@example.com");

        mockMvc.perform(post("/api/v1/student-enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enrollmentJson(studentId, "MATH-101", LocalDate.now())))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/api/v1/student-enrollments/")))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.studentId").value(studentId))
                .andExpect(jsonPath("$.courseCode").value("MATH-101"))
                .andExpect(jsonPath("$.enrollmentDate").value(LocalDate.now().toString()));
    }

    @Test
    void listsAllEnrollments() throws Exception {
        Long firstStudentId = createStudent("Ada", "Lovelace", "ada.enrollment.list@example.com");
        Long secondStudentId = createStudent("Grace", "Hopper", "grace.enrollment.list@example.com");
        createEnrollment(firstStudentId, "MATH-101");
        createEnrollment(secondStudentId, "CS-101");

        mockMvc.perform(get("/api/v1/student-enrollments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void listsEnrollmentsByStudentIdQueryParam() throws Exception {
        Long studentId = createStudent("Ada", "Lovelace", "ada.enrollment.query@example.com");
        Long otherStudentId = createStudent("Grace", "Hopper", "grace.enrollment.query@example.com");
        createEnrollment(studentId, "MATH-101");
        createEnrollment(otherStudentId, "CS-101");

        mockMvc.perform(get("/api/v1/student-enrollments").param("studentId", studentId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].studentId").value(studentId))
                .andExpect(jsonPath("$[0].courseCode").value("MATH-101"));
    }

    @Test
    void listsEnrollmentsByNestedStudentRoute() throws Exception {
        Long studentId = createStudent("Ada", "Lovelace", "ada.enrollment.nested@example.com");
        createEnrollment(studentId, "MATH-101");

        mockMvc.perform(get("/api/v1/students/{id}/enrollments", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].studentId").value(studentId));
    }

    @Test
    void getsEnrollmentById() throws Exception {
        Long studentId = createStudent("Ada", "Lovelace", "ada.enrollment.get@example.com");
        Long enrollmentId = createEnrollment(studentId, "MATH-101");

        mockMvc.perform(get("/api/v1/student-enrollments/{id}", enrollmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(enrollmentId))
                .andExpect(jsonPath("$.studentId").value(studentId));
    }

    @Test
    void updatesEnrollment() throws Exception {
        Long studentId = createStudent("Ada", "Lovelace", "ada.enrollment.update@example.com");
        Long enrollmentId = createEnrollment(studentId, "MATH-101");

        mockMvc.perform(put("/api/v1/student-enrollments/{id}", enrollmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "courseCode": "CS-101",
                                  "enrollmentDate": "%s"
                                }
                                """.formatted(LocalDate.now())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(enrollmentId))
                .andExpect(jsonPath("$.courseCode").value("CS-101"));
    }

    @Test
    void deletesEnrollment() throws Exception {
        Long studentId = createStudent("Ada", "Lovelace", "ada.enrollment.delete@example.com");
        Long enrollmentId = createEnrollment(studentId, "MATH-101");

        mockMvc.perform(delete("/api/v1/student-enrollments/{id}", enrollmentId))
                .andExpect(status().isNoContent());
    }

    @Test
    void returnsNotFoundForMissingEnrollment() throws Exception {
        mockMvc.perform(get("/api/v1/student-enrollments/{id}", 999999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Resource Not Found"))
                .andExpect(jsonPath("$.detail").value("Student enrollment not found with id: 999999"));
    }

    @Test
    void returnsNotFoundForMissingReferencedStudent() throws Exception {
        mockMvc.perform(post("/api/v1/student-enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enrollmentJson(999999L, "MATH-101", LocalDate.now())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Student not found with id: 999999"));
    }

    @Test
    void returnsBadRequestForInvalidEnrollmentRequest() throws Exception {
        mockMvc.perform(post("/api/v1/student-enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enrollmentJson(-1L, "", LocalDate.now().plusDays(1))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation Error"))
                .andExpect(jsonPath("$.errors").isArray());
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

    private Long createEnrollment(Long studentId, String courseCode) throws Exception {
        String response = mockMvc.perform(post("/api/v1/student-enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enrollmentJson(studentId, courseCode, LocalDate.now())))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return Long.valueOf(response.replaceAll(".*\\\"id\\\":(\\d+).*", "$1"));
    }

    private String enrollmentJson(Long studentId, String courseCode, LocalDate enrollmentDate) {
        return """
                {
                  "studentId": %d,
                  "courseCode": "%s",
                  "enrollmentDate": "%s"
                }
                """.formatted(studentId, courseCode, enrollmentDate);
    }
}
