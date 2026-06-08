package com.elliotgaramendi.studentenrollment.infrastructure.adapter.out.persistence.adapter;

import com.elliotgaramendi.studentenrollment.domain.model.Student;
import com.elliotgaramendi.studentenrollment.domain.model.StudentEnrollment;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.out.persistence.mapper.StudentEnrollmentPersistenceMapper;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.out.persistence.mapper.StudentPersistenceMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        StudentPersistenceAdapter.class,
        StudentEnrollmentPersistenceAdapter.class,
        StudentPersistenceMapper.class,
        StudentEnrollmentPersistenceMapper.class
})
class StudentEnrollmentPersistenceAdapterTest {

    @Autowired
    private StudentPersistenceAdapter studentPersistenceAdapter;

    @Autowired
    private StudentEnrollmentPersistenceAdapter studentEnrollmentPersistenceAdapter;

    @Test
    void savesAndFindsEnrollmentById() {
        Student student = studentPersistenceAdapter.save(
                new Student(null, "Elliot", "Garamendi", "elliot@example.com")
        );
        StudentEnrollment saved = studentEnrollmentPersistenceAdapter.save(
                new StudentEnrollment(null, student.getId(), "MATH-101", LocalDate.of(2026, 6, 7))
        );

        Optional<StudentEnrollment> result = studentEnrollmentPersistenceAdapter.findById(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getStudentId()).isEqualTo(student.getId());
        assertThat(result.get().getCourseCode()).isEqualTo("MATH-101");
        assertThat(result.get().getEnrollmentDate()).isEqualTo(LocalDate.of(2026, 6, 7));
    }

    @Test
    void listsEnrollmentsByStudentId() {
        Student student = studentPersistenceAdapter.save(
                new Student(null, "Ada", "Lovelace", "ada@example.com")
        );
        studentEnrollmentPersistenceAdapter.save(
                new StudentEnrollment(null, student.getId(), "CS-101", LocalDate.of(2026, 6, 7))
        );
        studentEnrollmentPersistenceAdapter.save(
                new StudentEnrollment(null, student.getId(), "CS-102", LocalDate.of(2026, 6, 8))
        );

        List<StudentEnrollment> result = studentEnrollmentPersistenceAdapter.findByStudentId(student.getId());

        assertThat(result).hasSize(2);
        assertThat(result).extracting(StudentEnrollment::getCourseCode).containsExactlyInAnyOrder("CS-101", "CS-102");
    }

    @Test
    void listsAllEnrollments() {
        Student firstStudent = studentPersistenceAdapter.save(
                new Student(null, "Ada", "Lovelace", "ada@example.com")
        );
        Student secondStudent = studentPersistenceAdapter.save(
                new Student(null, "Grace", "Hopper", "grace@example.com")
        );
        studentEnrollmentPersistenceAdapter.save(
                new StudentEnrollment(null, firstStudent.getId(), "CS-101", LocalDate.of(2026, 6, 7))
        );
        studentEnrollmentPersistenceAdapter.save(
                new StudentEnrollment(null, secondStudent.getId(), "CS-102", LocalDate.of(2026, 6, 8))
        );

        List<StudentEnrollment> result = studentEnrollmentPersistenceAdapter.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void deletesEnrollmentsByStudentId() {
        Student student = studentPersistenceAdapter.save(
                new Student(null, "Grace", "Hopper", "grace@example.com")
        );
        studentEnrollmentPersistenceAdapter.save(
                new StudentEnrollment(null, student.getId(), "CS-101", LocalDate.of(2026, 6, 7))
        );
        studentEnrollmentPersistenceAdapter.save(
                new StudentEnrollment(null, student.getId(), "CS-102", LocalDate.of(2026, 6, 8))
        );

        studentEnrollmentPersistenceAdapter.deleteByStudentId(student.getId());

        assertThat(studentEnrollmentPersistenceAdapter.findByStudentId(student.getId())).isEmpty();
    }
}
