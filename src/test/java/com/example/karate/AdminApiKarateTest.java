package com.example.karate;

import com.intuit.karate.junit5.Karate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("karate-admin")
@DisplayName("Admin API Karate Tests with Testcontainers")
public class AdminApiKarateTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("admin_test_db")
            .withUsername("postgres")
            .withPassword("postgres");

    @BeforeAll
    static void setup() {
        System.setProperty("karate.env", "karate-admin");
    }

    @Karate.Test
    public Karate testAdminApis() {
        return Karate.run("classpath:karate/admin.feature").relativeTo(getClass());
    }
}
