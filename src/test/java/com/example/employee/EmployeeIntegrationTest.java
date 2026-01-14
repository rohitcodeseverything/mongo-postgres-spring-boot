package com.example.employee;

import com.example.employee.entity.Employee;
import com.example.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Employee CRUD Operations Integration Tests with Flapdoodle")
class EmployeeIntegrationTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create employee successfully")
    void testCreateEmployee() {
        Employee employee = new Employee(
                "John", "Doe", "john.doe@example.com", "Engineering", 50000.0
        );

        Employee savedEmployee = employeeRepository.save(employee);

        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isNotNull();
        assertThat(savedEmployee.getFirstName()).isEqualTo("John");
        assertThat(savedEmployee.getLastName()).isEqualTo("Doe");
        assertThat(savedEmployee.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(savedEmployee.getDepartment()).isEqualTo("Engineering");
        assertThat(savedEmployee.getSalary()).isEqualTo(50000.0);
        assertThat(savedEmployee.getCreatedAt()).isNotNull();
        assertThat(savedEmployee.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should retrieve all employees")
    void testGetAllEmployees() {
        Employee emp1 = new Employee("John", "Doe", "john.doe@example.com", "Engineering", 50000.0);
        Employee emp2 = new Employee("Jane", "Smith", "jane.smith@example.com", "HR", 45000.0);
        Employee emp3 = new Employee("Bob", "Johnson", "bob.johnson@example.com", "Sales", 55000.0);

        employeeRepository.save(emp1);
        employeeRepository.save(emp2);
        employeeRepository.save(emp3);

        List<Employee> employees = employeeRepository.findAll();

        assertThat(employees).hasSize(3);
        assertThat(employees)
                .extracting(Employee::getFirstName)
                .containsExactlyInAnyOrder("John", "Jane", "Bob");
    }

    @Test
    @DisplayName("Should retrieve employee by ID")
    void testGetEmployeeById() {
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", "Engineering", 50000.0);
        Employee savedEmployee = employeeRepository.save(employee);

        Optional<Employee> foundEmployee = employeeRepository.findById(savedEmployee.getId());

        assertTrue(foundEmployee.isPresent());
        assertThat(foundEmployee.get().getId()).isEqualTo(savedEmployee.getId());
        assertThat(foundEmployee.get().getFirstName()).isEqualTo("John");
    }

    @Test
    @DisplayName("Should retrieve employee by email")
    void testGetEmployeeByEmail() {
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", "Engineering", 50000.0);
        employeeRepository.save(employee);

        Optional<Employee> foundEmployee = employeeRepository.findByEmail("john.doe@example.com");

        assertTrue(foundEmployee.isPresent());
        assertThat(foundEmployee.get().getFirstName()).isEqualTo("John");
        assertThat(foundEmployee.get().getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    @DisplayName("Should update employee successfully")
    void testUpdateEmployee() {
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", "Engineering", 50000.0);
        Employee savedEmployee = employeeRepository.save(employee);

        savedEmployee.setFirstName("Jane");
        savedEmployee.setDepartment("HR");
        savedEmployee.setSalary(55000.0);
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        assertThat(updatedEmployee.getFirstName()).isEqualTo("Jane");
        assertThat(updatedEmployee.getDepartment()).isEqualTo("HR");
        assertThat(updatedEmployee.getSalary()).isEqualTo(55000.0);
    }

    @Test
    @DisplayName("Should delete employee successfully")
    void testDeleteEmployee() {
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", "Engineering", 50000.0);
        Employee savedEmployee = employeeRepository.save(employee);

        employeeRepository.deleteById(savedEmployee.getId());

        Optional<Employee> deletedEmployee = employeeRepository.findById(savedEmployee.getId());

        assertThat(deletedEmployee).isEmpty();
    }

    @Test
    @DisplayName("Should return empty when retrieving non-existent employee")
    void testGetNonExistentEmployee() {
        Optional<Employee> employee = employeeRepository.findById("non-existent-id");

        assertThat(employee).isEmpty();
    }

    @Test
    @DisplayName("Should count employees correctly")
    void testCountEmployees() {
        employeeRepository.save(new Employee("John", "Doe", "john.doe@example.com", "Engineering", 50000.0));
        employeeRepository.save(new Employee("Jane", "Smith", "jane.smith@example.com", "HR", 45000.0));

        long count = employeeRepository.count();

        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Should check if employee exists by ID")
    void testEmployeeExists() {
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", "Engineering", 50000.0);
        Employee savedEmployee = employeeRepository.save(employee);

        boolean exists = employeeRepository.existsById(savedEmployee.getId());

        assertTrue(exists);
    }

    @Test
    @DisplayName("Should delete all employees")
    void testDeleteAllEmployees() {
        employeeRepository.save(new Employee("John", "Doe", "john.doe@example.com", "Engineering", 50000.0));
        employeeRepository.save(new Employee("Jane", "Smith", "jane.smith@example.com", "HR", 45000.0));

        employeeRepository.deleteAll();

        long count = employeeRepository.count();

        assertThat(count).isEqualTo(0);
    }
}
