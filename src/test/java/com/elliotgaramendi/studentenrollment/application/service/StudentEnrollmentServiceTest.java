package com.elliotgaramendi.studentenrollment.application.service;

import com.elliotgaramendi.studentenrollment.application.exception.ResourceNotFoundException;
import com.elliotgaramendi.studentenrollment.domain.model.StudentEnrollment;
import com.elliotgaramendi.studentenrollment.domain.port.in.command.CreateStudentEnrollmentCommand;
import com.elliotgaramendi.studentenrollment.domain.port.in.command.UpdateStudentEnrollmentCommand;
import com.elliotgaramendi.studentenrollment.domain.port.out.StudentEnrollmentRepositoryPort;
import com.elliotgaramendi.studentenrollment.domain.port.out.StudentRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentEnrollmentServiceTest {

    @Mock
    private StudentEnrollmentRepositoryPort studentEnrollmentRepositoryPort;

    @Mock
    private StudentRepositoryPort studentRepositoryPort;

    private StudentEnrollmentService studentEnrollmentService;

    @BeforeEach
    void setUp() {
        studentEnrollmentService = new StudentEnrollmentService(
                studentEnrollmentRepositoryPort,
                studentRepositoryPort
        );
    }

    @Test
    void createsEnrollmentOnlyWhenReferencedStudentExists() {
        LocalDate enrollmentDate = LocalDate.of(2026, 6, 7);
        CreateStudentEnrollmentCommand command = new CreateStudentEnrollmentCommand(1L, "MATH-101", enrollmentDate);
        StudentEnrollment savedEnrollment = new StudentEnrollment(10L, 1L, "MATH-101", enrollmentDate);
        when(studentRepositoryPort.existsById(1L)).thenReturn(true);
        when(studentEnrollmentRepositoryPort.save(org.mockito.ArgumentMatchers.any(StudentEnrollment.class)))
                .thenReturn(savedEnrollment);

        StudentEnrollment result = studentEnrollmentService.createEnrollment(command);

        ArgumentCaptor<StudentEnrollment> captor = ArgumentCaptor.forClass(StudentEnrollment.class);
        verify(studentEnrollmentRepositoryPort).save(captor.capture());
        StudentEnrollment enrollmentToSave = captor.getValue();
        assertThat(enrollmentToSave.getId()).isNull();
        assertThat(enrollmentToSave.getStudentId()).isEqualTo(1L);
        assertThat(enrollmentToSave.getCourseCode()).isEqualTo("MATH-101");
        assertThat(enrollmentToSave.getEnrollmentDate()).isEqualTo(enrollmentDate);
        assertThat(result).isEqualTo(savedEnrollment);
    }

    @Test
    void throwsResourceNotFoundWhenReferencedStudentDoesNotExist() {
        CreateStudentEnrollmentCommand command = new CreateStudentEnrollmentCommand(
                99L,
                "MATH-101",
                LocalDate.of(2026, 6, 7)
        );
        when(studentRepositoryPort.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> studentEnrollmentService.createEnrollment(command))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Student not found with id: 99");
        verifyNoMoreInteractions(studentEnrollmentRepositoryPort);
    }

    @Test
    void returnsEnrollmentByIdWhenFound() {
        StudentEnrollment enrollment = new StudentEnrollment(10L, 1L, "MATH-101", LocalDate.of(2026, 6, 7));
        when(studentEnrollmentRepositoryPort.findById(10L)).thenReturn(Optional.of(enrollment));

        StudentEnrollment result = studentEnrollmentService.getEnrollmentById(10L);

        assertThat(result).isEqualTo(enrollment);
    }

    @Test
    void throwsResourceNotFoundWhenEnrollmentIsMissing() {
        when(studentEnrollmentRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentEnrollmentService.getEnrollmentById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Student enrollment not found with id: 99");
    }

    @Test
    void listsEnrollments() {
        List<StudentEnrollment> enrollments = List.of(
                new StudentEnrollment(10L, 1L, "MATH-101", LocalDate.of(2026, 6, 7)),
                new StudentEnrollment(11L, 2L, "CS-101", LocalDate.of(2026, 6, 8))
        );
        when(studentEnrollmentRepositoryPort.findAll()).thenReturn(enrollments);

        List<StudentEnrollment> result = studentEnrollmentService.listEnrollments();

        assertThat(result).containsExactlyElementsOf(enrollments);
    }

    @Test
    void returnsEnrollmentsByStudentIdAfterValidatingStudentExists() {
        List<StudentEnrollment> enrollments = List.of(
                new StudentEnrollment(10L, 1L, "MATH-101", LocalDate.of(2026, 6, 7))
        );
        when(studentRepositoryPort.existsById(1L)).thenReturn(true);
        when(studentEnrollmentRepositoryPort.findByStudentId(1L)).thenReturn(enrollments);

        List<StudentEnrollment> result = studentEnrollmentService.listEnrollmentsByStudentId(1L);

        assertThat(result).containsExactlyElementsOf(enrollments);
    }

    @Test
    void updatesExistingEnrollment() {
        StudentEnrollment enrollment = new StudentEnrollment(10L, 1L, "OLD-101", LocalDate.of(2026, 6, 7));
        UpdateStudentEnrollmentCommand command = new UpdateStudentEnrollmentCommand(
                "MATH-101",
                LocalDate.of(2026, 6, 8)
        );
        when(studentEnrollmentRepositoryPort.findById(10L)).thenReturn(Optional.of(enrollment));
        when(studentEnrollmentRepositoryPort.save(enrollment)).thenReturn(enrollment);

        StudentEnrollment result = studentEnrollmentService.updateEnrollment(10L, command);

        assertThat(result.getCourseCode()).isEqualTo("MATH-101");
        assertThat(result.getEnrollmentDate()).isEqualTo(LocalDate.of(2026, 6, 8));
        verify(studentEnrollmentRepositoryPort).save(enrollment);
    }

    @Test
    void deletesExistingEnrollment() {
        StudentEnrollment enrollment = new StudentEnrollment(10L, 1L, "MATH-101", LocalDate.of(2026, 6, 7));
        when(studentEnrollmentRepositoryPort.findById(10L)).thenReturn(Optional.of(enrollment));

        studentEnrollmentService.deleteEnrollment(10L);

        verify(studentEnrollmentRepositoryPort).deleteById(10L);
    }

    @Test
    void throwsResourceNotFoundBeforeDeletingMissingEnrollment() {
        when(studentEnrollmentRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentEnrollmentService.deleteEnrollment(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Student enrollment not found with id: 99");
    }
}
