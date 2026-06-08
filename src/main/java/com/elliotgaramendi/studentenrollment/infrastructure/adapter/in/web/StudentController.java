package com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web;

import com.elliotgaramendi.studentenrollment.domain.port.in.StudentEnrollmentUseCase;
import com.elliotgaramendi.studentenrollment.domain.port.in.StudentUseCase;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto.CreateStudentRequest;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto.StudentEnrollmentResponse;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto.StudentResponse;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto.UpdateStudentRequest;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.mapper.StudentEnrollmentWebMapper;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.mapper.StudentWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ProblemDetail;
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
@Tag(name = "Students", description = "Operations for creating, reading, updating, and deleting students.")
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

    @Operation(
            summary = "Create a student",
            description = "Creates a new student. Student emails must be unique.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Student created successfully",
                            content = @Content(schema = @Schema(implementation = StudentResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request body",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "409", description = "Student email already exists",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody CreateStudentRequest request) {
        StudentResponse response = studentWebMapper.toResponse(
                studentUseCase.createStudent(studentWebMapper.toCommand(request))
        );
        return ResponseEntity.created(URI.create("/api/v1/students/" + response.id())).body(response);
    }

    @Operation(
            summary = "List students",
            description = "Returns every registered student.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Students returned successfully",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = StudentResponse.class)))),
                    @ApiResponse(responseCode = "500", description = "Unexpected server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<StudentResponse>> listStudents() {
        List<StudentResponse> response = studentUseCase.listStudents()
                .stream()
                .map(studentWebMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get a student by id",
            description = "Returns one student by its generated identifier.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Student returned successfully",
                            content = @Content(schema = @Schema(implementation = StudentResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid student id",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "404", description = "Student not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(
            @Parameter(description = "Generated student id", example = "1", required = true)
            @PathVariable @Positive Long id
    ) {
        return ResponseEntity.ok(studentWebMapper.toResponse(studentUseCase.getStudentById(id)));
    }

    @Operation(
            summary = "Update a student",
            description = "Updates a student's profile. Student emails must remain unique.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Student updated successfully",
                            content = @Content(schema = @Schema(implementation = StudentResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request body or student id",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "404", description = "Student not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "409", description = "Student email already exists",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @Parameter(description = "Generated student id", example = "1", required = true)
            @PathVariable @Positive Long id,
            @Valid @RequestBody UpdateStudentRequest request
    ) {
        StudentResponse response = studentWebMapper.toResponse(
                studentUseCase.updateStudent(id, studentWebMapper.toCommand(request))
        );
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete a student",
            description = "Deletes a student and its related enrollments.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Student deleted successfully",
                            content = @Content),
                    @ApiResponse(responseCode = "400", description = "Invalid student id",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "404", description = "Student not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @Parameter(description = "Generated student id", example = "1", required = true)
            @PathVariable @Positive Long id
    ) {
        studentUseCase.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "List enrollments for a student",
            description = "Returns all enrollments associated with a specific student.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Student enrollments returned successfully",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = StudentEnrollmentResponse.class)))),
                    @ApiResponse(responseCode = "400", description = "Invalid student id",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "404", description = "Student not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @GetMapping("/{id}/enrollments")
    public ResponseEntity<List<StudentEnrollmentResponse>> listEnrollmentsByStudentId(
            @Parameter(description = "Generated student id", example = "1", required = true)
            @PathVariable @Positive Long id
    ) {
        List<StudentEnrollmentResponse> response = studentEnrollmentUseCase.listEnrollmentsByStudentId(id)
                .stream()
                .map(studentEnrollmentWebMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }
}
