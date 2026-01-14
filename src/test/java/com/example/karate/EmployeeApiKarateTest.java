package com.example.karate;

import com.intuit.karate.junit5.Karate;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("karate")
@DisplayName("Employee API Karate Tests with Flapdoodle")
public class EmployeeApiKarateTest {

    @Karate.Test
    public Karate testEmployeeApis() {
        return Karate.run("classpath:karate/employee.feature").relativeTo(getClass());
    }
}
