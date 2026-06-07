package com.example.studentenrollment.domain.model;

import java.time.LocalDate;

public class StudentEnrollment {

    private final Long id;
    private final Long studentId;
    private String courseCode;
    private LocalDate enrollmentDate;

    public StudentEnrollment(Long id, Long studentId, String courseCode, LocalDate enrollmentDate) {
        this.id = id;
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.enrollmentDate = enrollmentDate;
    }

    public Long getId() {
        return id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void updateEnrollment(String courseCode, LocalDate enrollmentDate) {
        this.courseCode = courseCode;
        this.enrollmentDate = enrollmentDate;
    }
}
