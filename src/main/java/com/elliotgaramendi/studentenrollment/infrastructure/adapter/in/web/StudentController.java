package com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web;

import com.elliotgaramendi.studentenrollment.domain.port.in.StudentEnrollmentUseCase;
import com.elliotgaramendi.studentenrollment.domain.port.in.StudentUseCase;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto.CreateStudentRequest;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto.StudentEnrollmentResponse;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto.StudentResponse;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto.UpdateStudentRequest;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.mapper.StudentEnrollmentWebMapper;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.mapper.StudentWebMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentUseCase studentUseCase;
    private final StudentEnrollmentUseCase studentEnrollmentUseCase;
    private final StudentWebMapper studentWebMapper;
    private final StudentEnrollmentWebMapper studentEnrollmentWebMapper;

    public StudentController(
            StudentUseCase studentUseCase,
            StudentEnrollmentUseCase studentEnrollmentUseCase,
            StudentWebMapper studentWebMapper,
            StudentEnrollmentWebMapper studentEnrollmentWebMapper
    ) {
        this.studentUseCase = studentUseCase;
        this.studentEnrollmentUseCase = studentEnrollmentUseCase;
        this.studentWebMapper = studentWebMapper;
        this.studentEnrollmentWebMapper = studentEnrollmentWebMapper;
    }

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody CreateStudentRequest request) {
        StudentResponse response = studentWebMapper.toResponse(
                studentUseCase.createStudent(studentWebMapper.toCommand(request))
        );
        return ResponseEntity.created(URI.create("/api/v1/students/" + response.id())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> listStudents() {
        List<StudentResponse> response = studentUseCase.listStudents()
                .stream()
                .map(studentWebMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(studentWebMapper.toResponse(studentUseCase.getStudentById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable @Positive Long id,
            @Valid @RequestBody UpdateStudentRequest request
    ) {
        StudentResponse response = studentWebMapper.toResponse(
                studentUseCase.updateStudent(id, studentWebMapper.toCommand(request))
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable @Positive Long id) {
        studentUseCase.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/enrollments")
    public ResponseEntity<List<StudentEnrollmentResponse>> listEnrollmentsByStudentId(
            @PathVariable @Positive Long id
    ) {
        List<StudentEnrollmentResponse> response = studentEnrollmentUseCase.listEnrollmentsByStudentId(id)
                .stream()
                .map(studentEnrollmentWebMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }
}
