package com.elliotgaramendi.studentenrollment.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "student_enrollments")
public class StudentEnrollmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "student_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_student_enrollments_student")
    )
    private StudentEntity student;

    @Column(name = "course_code", nullable = false, length = 50)
    private String courseCode;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    protected StudentEnrollmentEntity() {
    }

    public StudentEnrollmentEntity(Long id, StudentEntity student, String courseCode, LocalDate enrollmentDate) {
        this.id = id;
        this.student = student;
        this.courseCode = courseCode;
        this.enrollmentDate = enrollmentDate;
    }

    public Long getId() {
        return id;
    }

    public StudentEntity getStudent() {
        return student;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }
}
