package com.elliotgaramendi.studentenrollment.application.service;

import com.elliotgaramendi.studentenrollment.application.exception.DuplicateResourceException;
import com.elliotgaramendi.studentenrollment.application.exception.ResourceNotFoundException;
import com.elliotgaramendi.studentenrollment.domain.model.Student;
import com.elliotgaramendi.studentenrollment.domain.port.in.command.CreateStudentCommand;
import com.elliotgaramendi.studentenrollment.domain.port.in.command.UpdateStudentCommand;
import com.elliotgaramendi.studentenrollment.domain.port.out.StudentEnrollmentRepositoryPort;
import com.elliotgaramendi.studentenrollment.domain.port.out.StudentRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepositoryPort studentRepositoryPort;

    @Mock
    private StudentEnrollmentRepositoryPort studentEnrollmentRepositoryPort;

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepositoryPort, studentEnrollmentRepositoryPort);
    }

    @Test
    void createsStudentThroughRepositoryPort() {
        CreateStudentCommand command = new CreateStudentCommand("Elliot", "Garamendi", "elliot@example.com");
        Student savedStudent = new Student(1L, "Elliot", "Garamendi", "elliot@example.com");
        when(studentRepositoryPort.findByEmail("elliot@example.com")).thenReturn(Optional.empty());
        when(studentRepositoryPort.save(org.mockito.ArgumentMatchers.any(Student.class))).thenReturn(savedStudent);

        Student result = studentService.createStudent(command);

        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepositoryPort).save(captor.capture());
        Student studentToSave = captor.getValue();
        assertThat(studentToSave.getId()).isNull();
        assertThat(studentToSave.getFirstName()).isEqualTo("Elliot");
        assertThat(studentToSave.getLastName()).isEqualTo("Garamendi");
        assertThat(studentToSave.getEmail()).isEqualTo("elliot@example.com");
        assertThat(result).isEqualTo(savedStudent);
    }

    @Test
    void returnsStudentByIdWhenFound() {
        Student student = new Student(1L, "Elliot", "Garamendi", "elliot@example.com");
        when(studentRepositoryPort.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1L);

        assertThat(result).isEqualTo(student);
    }

    @Test
    void throwsResourceNotFoundWhenStudentIsMissing() {
        when(studentRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getStudentById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Student not found with id: 99");
    }

    @Test
    void listsStudents() {
        List<Student> students = List.of(
                new Student(1L, "Elliot", "Garamendi", "elliot@example.com"),
                new Student(2L, "Ada", "Lovelace", "ada@example.com")
        );
        when(studentRepositoryPort.findAll()).thenReturn(students);

        List<Student> result = studentService.listStudents();

        assertThat(result).containsExactlyElementsOf(students);
    }

    @Test
    void updatesExistingStudent() {
        Student student = new Student(1L, "Old", "Name", "old@example.com");
        UpdateStudentCommand command = new UpdateStudentCommand("Elliot", "Garamendi", "elliot@example.com");
        when(studentRepositoryPort.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepositoryPort.findByEmail("elliot@example.com")).thenReturn(Optional.empty());
        when(studentRepositoryPort.save(student)).thenReturn(student);

        Student result = studentService.updateStudent(1L, command);

        assertThat(result.getFirstName()).isEqualTo("Elliot");
        assertThat(result.getLastName()).isEqualTo("Garamendi");
        assertThat(result.getEmail()).isEqualTo("elliot@example.com");
        verify(studentRepositoryPort).save(student);
    }

    @Test
    void throwsDuplicateResourceWhenCreatingStudentWithExistingEmail() {
        CreateStudentCommand command = new CreateStudentCommand("Elliot", "Garamendi", "elliot@example.com");
        when(studentRepositoryPort.findByEmail("elliot@example.com"))
                .thenReturn(Optional.of(new Student(1L, "Existing", "Student", "elliot@example.com")));

        assertThatThrownBy(() -> studentService.createStudent(command))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Student email already exists: elliot@example.com");
    }

    @Test
    void throwsDuplicateResourceWhenUpdatingStudentWithAnotherStudentEmail() {
        Student student = new Student(1L, "Old", "Name", "old@example.com");
        UpdateStudentCommand command = new UpdateStudentCommand("Elliot", "Garamendi", "elliot@example.com");
        when(studentRepositoryPort.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepositoryPort.findByEmail("elliot@example.com"))
                .thenReturn(Optional.of(new Student(2L, "Other", "Student", "elliot@example.com")));

        assertThatThrownBy(() -> studentService.updateStudent(1L, command))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Student email already exists: elliot@example.com");
    }

    @Test
    void deletesStudentAndRelatedEnrollments() {
        when(studentRepositoryPort.existsById(1L)).thenReturn(true);

        studentService.deleteStudent(1L);

        verify(studentEnrollmentRepositoryPort).deleteByStudentId(1L);
        verify(studentRepositoryPort).deleteById(1L);
    }

    @Test
    void throwsResourceNotFoundBeforeDeletingMissingStudent() {
        when(studentRepositoryPort.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> studentService.deleteStudent(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Student not found with id: 99");
        verifyNoInteractions(studentEnrollmentRepositoryPort);
    }
}
