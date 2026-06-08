package com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web;

import com.elliotgaramendi.studentenrollment.domain.model.StudentEnrollment;
import com.elliotgaramendi.studentenrollment.domain.port.in.StudentEnrollmentUseCase;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto.CreateStudentEnrollmentRequest;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto.StudentEnrollmentResponse;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto.UpdateStudentEnrollmentRequest;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.mapper.StudentEnrollmentWebMapper;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/student-enrollments")
public class StudentEnrollmentController {

    private final StudentEnrollmentUseCase studentEnrollmentUseCase;
    private final StudentEnrollmentWebMapper studentEnrollmentWebMapper;

    public StudentEnrollmentController(
            StudentEnrollmentUseCase studentEnrollmentUseCase,
            StudentEnrollmentWebMapper studentEnrollmentWebMapper
    ) {
        this.studentEnrollmentUseCase = studentEnrollmentUseCase;
        this.studentEnrollmentWebMapper = studentEnrollmentWebMapper;
    }

    @PostMapping
    public ResponseEntity<StudentEnrollmentResponse> createEnrollment(
            @Valid @RequestBody CreateStudentEnrollmentRequest request
    ) {
        StudentEnrollmentResponse response = studentEnrollmentWebMapper.toResponse(
                studentEnrollmentUseCase.createEnrollment(studentEnrollmentWebMapper.toCommand(request))
        );
        return ResponseEntity.created(URI.create("/api/v1/student-enrollments/" + response.id())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<StudentEnrollmentResponse>> listEnrollments(
            @RequestParam(required = false) @Positive Long studentId
    ) {
        List<StudentEnrollment> enrollments = studentId == null
                ? studentEnrollmentUseCase.listEnrollments()
                : studentEnrollmentUseCase.listEnrollmentsByStudentId(studentId);

        List<StudentEnrollmentResponse> response = enrollments.stream()
                .map(studentEnrollmentWebMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentEnrollmentResponse> getEnrollmentById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(studentEnrollmentWebMapper.toResponse(studentEnrollmentUseCase.getEnrollmentById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentEnrollmentResponse> updateEnrollment(
            @PathVariable @Positive Long id,
            @Valid @RequestBody UpdateStudentEnrollmentRequest request
    ) {
        StudentEnrollmentResponse response = studentEnrollmentWebMapper.toResponse(
                studentEnrollmentUseCase.updateEnrollment(id, studentEnrollmentWebMapper.toCommand(request))
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable @Positive Long id) {
        studentEnrollmentUseCase.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }
}
