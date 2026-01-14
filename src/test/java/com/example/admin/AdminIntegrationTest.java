package com.example.admin;

import com.example.admin.entity.Admin;
import com.example.admin.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test-postgres")
@DisplayName("Admin CRUD Operations Integration Tests with Testcontainers")
class AdminIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("admin_test_db")
            .withUsername("admin")
            .withPassword("admin123");

    @Autowired
    private AdminRepository adminRepository;

    @BeforeEach
    void setUp() {
        adminRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create admin successfully")
    void testCreateAdmin() {
        Admin admin = new Admin("admin01", "admin01@example.com", "password123", "John Admin", "SUPER_ADMIN");

        Admin savedAdmin = adminRepository.save(admin);

        assertThat(savedAdmin).isNotNull();
        assertThat(savedAdmin.getId()).isNotNull();
        assertThat(savedAdmin.getUsername()).isEqualTo("admin01");
        assertThat(savedAdmin.getEmail()).isEqualTo("admin01@example.com");
        assertThat(savedAdmin.getPassword()).isEqualTo("password123");
        assertThat(savedAdmin.getFullName()).isEqualTo("John Admin");
        assertThat(savedAdmin.getRole()).isEqualTo("SUPER_ADMIN");
        assertThat(savedAdmin.getActive()).isTrue();
        assertThat(savedAdmin.getCreatedAt()).isNotNull();
        assertThat(savedAdmin.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should retrieve all admins")
    void testGetAllAdmins() {
        Admin admin1 = new Admin("admin01", "admin01@example.com", "password123", "John Admin", "SUPER_ADMIN");
        Admin admin2 = new Admin("admin02", "admin02@example.com", "password456", "Jane Admin", "ADMIN");
        Admin admin3 = new Admin("admin03", "admin03@example.com", "password789", "Bob Admin", "MODERATOR");

        adminRepository.save(admin1);
        adminRepository.save(admin2);
        adminRepository.save(admin3);

        List<Admin> admins = adminRepository.findAll();

        assertThat(admins).hasSize(3);
        assertThat(admins)
                .extracting(Admin::getUsername)
                .containsExactlyInAnyOrder("admin01", "admin02", "admin03");
    }

    @Test
    @DisplayName("Should retrieve admin by ID")
    void testGetAdminById() {
        Admin admin = new Admin("admin01", "admin01@example.com", "password123", "John Admin", "SUPER_ADMIN");
        Admin savedAdmin = adminRepository.save(admin);

        Optional<Admin> foundAdmin = adminRepository.findById(savedAdmin.getId());

        assertTrue(foundAdmin.isPresent());
        assertThat(foundAdmin.get().getId()).isEqualTo(savedAdmin.getId());
        assertThat(foundAdmin.get().getUsername()).isEqualTo("admin01");
    }

    @Test
    @DisplayName("Should retrieve admin by username")
    void testGetAdminByUsername() {
        Admin admin = new Admin("admin01", "admin01@example.com", "password123", "John Admin", "SUPER_ADMIN");
        adminRepository.save(admin);

        Optional<Admin> foundAdmin = adminRepository.findByUsername("admin01");

        assertTrue(foundAdmin.isPresent());
        assertThat(foundAdmin.get().getUsername()).isEqualTo("admin01");
        assertThat(foundAdmin.get().getEmail()).isEqualTo("admin01@example.com");
    }

    @Test
    @DisplayName("Should retrieve admin by email")
    void testGetAdminByEmail() {
        Admin admin = new Admin("admin01", "admin01@example.com", "password123", "John Admin", "SUPER_ADMIN");
        adminRepository.save(admin);

        Optional<Admin> foundAdmin = adminRepository.findByEmail("admin01@example.com");

        assertTrue(foundAdmin.isPresent());
        assertThat(foundAdmin.get().getEmail()).isEqualTo("admin01@example.com");
        assertThat(foundAdmin.get().getUsername()).isEqualTo("admin01");
    }

    @Test
    @DisplayName("Should update admin successfully")
    void testUpdateAdmin() {
        Admin admin = new Admin("admin01", "admin01@example.com", "password123", "John Admin", "SUPER_ADMIN");
        Admin savedAdmin = adminRepository.save(admin);

        savedAdmin.setFullName("John Updated");
        savedAdmin.setRole("ADMIN");
        savedAdmin.setPassword("newpassword123");
        Admin updatedAdmin = adminRepository.save(savedAdmin);

        assertThat(updatedAdmin.getFullName()).isEqualTo("John Updated");
        assertThat(updatedAdmin.getRole()).isEqualTo("ADMIN");
        assertThat(updatedAdmin.getPassword()).isEqualTo("newpassword123");
    }

    @Test
    @DisplayName("Should delete admin successfully")
    void testDeleteAdmin() {
        Admin admin = new Admin("admin01", "admin01@example.com", "password123", "John Admin", "SUPER_ADMIN");
        Admin savedAdmin = adminRepository.save(admin);

        adminRepository.deleteById(savedAdmin.getId());

        Optional<Admin> deletedAdmin = adminRepository.findById(savedAdmin.getId());

        assertThat(deletedAdmin).isEmpty();
    }

    @Test
    @DisplayName("Should return empty when retrieving non-existent admin")
    void testGetNonExistentAdmin() {
        Optional<Admin> admin = adminRepository.findById(999L);

        assertThat(admin).isEmpty();
    }

    @Test
    @DisplayName("Should count admins correctly")
    void testCountAdmins() {
        adminRepository.save(new Admin("admin01", "admin01@example.com", "password123", "John Admin", "SUPER_ADMIN"));
        adminRepository.save(new Admin("admin02", "admin02@example.com", "password456", "Jane Admin", "ADMIN"));

        long count = adminRepository.count();

        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Should check if admin exists by ID")
    void testAdminExists() {
        Admin admin = new Admin("admin01", "admin01@example.com", "password123", "John Admin", "SUPER_ADMIN");
        Admin savedAdmin = adminRepository.save(admin);

        boolean exists = adminRepository.existsById(savedAdmin.getId());

        assertTrue(exists);
    }

    @Test
    @DisplayName("Should delete all admins")
    void testDeleteAllAdmins() {
        adminRepository.save(new Admin("admin01", "admin01@example.com", "password123", "John Admin", "SUPER_ADMIN"));
        adminRepository.save(new Admin("admin02", "admin02@example.com", "password456", "Jane Admin", "ADMIN"));

        adminRepository.deleteAll();

        long count = adminRepository.count();

        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("Should verify Testcontainers PostgreSQL is running")
    void testContainerIsRunning() {
        assertTrue(postgres.isRunning());
        assertThat(postgres.getDatabaseName()).isEqualTo("admin_test_db");
        assertThat(postgres.getUsername()).isEqualTo("admin");
    }

    @Test
    @DisplayName("Should deactivate admin")
    void testDeactivateAdmin() {
        Admin admin = new Admin("admin01", "admin01@example.com", "password123", "John Admin", "SUPER_ADMIN");
        Admin savedAdmin = adminRepository.save(admin);

        savedAdmin.setActive(false);
        Admin deactivatedAdmin = adminRepository.save(savedAdmin);

        assertThat(deactivatedAdmin.getActive()).isFalse();
    }
}
