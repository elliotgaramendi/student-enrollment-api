package com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web;

import com.elliotgaramendi.studentenrollment.domain.model.StudentEnrollment;
import com.elliotgaramendi.studentenrollment.domain.port.in.StudentEnrollmentUseCase;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto.CreateStudentEnrollmentRequest;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto.StudentEnrollmentResponse;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.dto.UpdateStudentEnrollmentRequest;
import com.elliotgaramendi.studentenrollment.infrastructure.adapter.in.web.mapper.StudentEnrollmentWebMapper;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/student-enrollments")
@Tag(name = "Student Enrollments", description = "Operations for managing course enrollments for students.")
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

    @Operation(
            summary = "Create a student enrollment",
            description = "Creates an enrollment for an existing student.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Enrollment created successfully",
                            content = @Content(schema = @Schema(implementation = StudentEnrollmentResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request body",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "404", description = "Referenced student not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @PostMapping
    public ResponseEntity<StudentEnrollmentResponse> createEnrollment(
            @Valid @RequestBody CreateStudentEnrollmentRequest request
    ) {
        StudentEnrollmentResponse response = studentEnrollmentWebMapper.toResponse(
                studentEnrollmentUseCase.createEnrollment(studentEnrollmentWebMapper.toCommand(request))
        );
        return ResponseEntity.created(URI.create("/api/v1/student-enrollments/" + response.id())).body(response);
    }

    @Operation(
            summary = "List student enrollments",
            description = "Returns every enrollment or filters by student id when the query parameter is provided.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Enrollments returned successfully",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = StudentEnrollmentResponse.class)))),
                    @ApiResponse(responseCode = "400", description = "Invalid student id query parameter",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "404", description = "Student not found when filtering by student id",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<StudentEnrollmentResponse>> listEnrollments(
            @Parameter(description = "Optional generated student id used to filter enrollments", example = "1")
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

    @Operation(
            summary = "Get a student enrollment by id",
            description = "Returns one enrollment by its generated identifier.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Enrollment returned successfully",
                            content = @Content(schema = @Schema(implementation = StudentEnrollmentResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid enrollment id",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "404", description = "Enrollment not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<StudentEnrollmentResponse> getEnrollmentById(
            @Parameter(description = "Generated enrollment id", example = "1", required = true)
            @PathVariable @Positive Long id
    ) {
        return ResponseEntity.ok(studentEnrollmentWebMapper.toResponse(studentEnrollmentUseCase.getEnrollmentById(id)));
    }

    @Operation(
            summary = "Update a student enrollment",
            description = "Updates course code and enrollment date for an existing enrollment.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Enrollment updated successfully",
                            content = @Content(schema = @Schema(implementation = StudentEnrollmentResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request body or enrollment id",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "404", description = "Enrollment not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<StudentEnrollmentResponse> updateEnrollment(
            @Parameter(description = "Generated enrollment id", example = "1", required = true)
            @PathVariable @Positive Long id,
            @Valid @RequestBody UpdateStudentEnrollmentRequest request
    ) {
        StudentEnrollmentResponse response = studentEnrollmentWebMapper.toResponse(
                studentEnrollmentUseCase.updateEnrollment(id, studentEnrollmentWebMapper.toCommand(request))
        );
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete a student enrollment",
            description = "Deletes an enrollment by its generated identifier.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Enrollment deleted successfully",
                            content = @Content),
                    @ApiResponse(responseCode = "400", description = "Invalid enrollment id",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "404", description = "Enrollment not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(
            @Parameter(description = "Generated enrollment id", example = "1", required = true)
            @PathVariable @Positive Long id
    ) {
        studentEnrollmentUseCase.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }
}
