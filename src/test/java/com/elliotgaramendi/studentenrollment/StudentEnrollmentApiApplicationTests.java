package com.elliotgaramendi.studentenrollment;

import com.elliotgaramendi.studentenrollment.domain.port.out.StudentEnrollmentRepositoryPort;
import com.elliotgaramendi.studentenrollment.domain.port.out.StudentRepositoryPort;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class StudentEnrollmentApiApplicationTests {

    @MockitoBean
    private StudentRepositoryPort studentRepositoryPort;

    @MockitoBean
    private StudentEnrollmentRepositoryPort studentEnrollmentRepositoryPort;

    @Test
    void contextLoads() {
    }
}
