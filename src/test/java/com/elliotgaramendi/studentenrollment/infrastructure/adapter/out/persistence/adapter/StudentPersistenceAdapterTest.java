package com.elliotgaramendi.studentenrollment.infrastructure.adapter.out.persistence.adapter;

import com.elliotgaramendi.studentenrollment.domain.model.Student;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.out.persistence.mapper.StudentPersistenceMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({StudentPersistenceAdapter.class, StudentPersistenceMapper.class})
class StudentPersistenceAdapterTest {

    @Autowired
    private StudentPersistenceAdapter studentPersistenceAdapter;

    @Test
    void savesAndFindsStudentById() {
        Student saved = studentPersistenceAdapter.save(
                new Student(null, "Elliot", "Garamendi", "elliot@example.com")
        );

        Optional<Student> result = studentPersistenceAdapter.findById(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("Elliot");
        assertThat(result.get().getLastName()).isEqualTo("Garamendi");
        assertThat(result.get().getEmail()).isEqualTo("elliot@example.com");
    }

    @Test
    void findsStudentByEmail() {
        studentPersistenceAdapter.save(new Student(null, "Ada", "Lovelace", "ada@example.com"));

        Optional<Student> result = studentPersistenceAdapter.findByEmail("ada@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("ada@example.com");
    }

    @Test
    void listsStudents() {
        studentPersistenceAdapter.save(new Student(null, "Ada", "Lovelace", "ada@example.com"));
        studentPersistenceAdapter.save(new Student(null, "Grace", "Hopper", "grace@example.com"));

        List<Student> result = studentPersistenceAdapter.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void checksStudentExistence() {
        Student saved = studentPersistenceAdapter.save(
                new Student(null, "Elliot", "Garamendi", "elliot@example.com")
        );

        assertThat(studentPersistenceAdapter.existsById(saved.getId())).isTrue();
        assertThat(studentPersistenceAdapter.existsById(999L)).isFalse();
    }
}
