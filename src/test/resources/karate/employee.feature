Feature: Employee API CRUD Operations

  Background:
    * url 'http://localhost:8080/api'
    * header Content-Type = 'application/json'

  Scenario: Create a new employee
    Given path '/employees'
    And request { firstName: 'John', lastName: 'Doe', email: 'john.doe@example.com', department: 'Engineering', salary: 50000.0 }
    When method post
    Then status 201
    And match response.firstName == 'John'
    And match response.lastName == 'Doe'
    And match response.email == 'john.doe@example.com'
    And match response.department == 'Engineering'
    And match response.salary == 50000.0
    And match response.id != null
    * def employeeId = response.id

  Scenario: Get all employees
    Given path '/employees'
    When method get
    Then status 200
    And match response == '#[]'

  Scenario: Create employee and retrieve by ID
    Given path '/employees'
    And request { firstName: 'Jane', lastName: 'Smith', email: 'jane.smith@example.com', department: 'HR', salary: 45000.0 }
    When method post
    Then status 201
    * def employeeId = response.id

    Given path '/employees/' + employeeId
    When method get
    Then status 200
    And match response.firstName == 'Jane'
    And match response.lastName == 'Smith'
    And match response.email == 'jane.smith@example.com'
    And match response.id == employeeId

  Scenario: Update an employee
    Given path '/employees'
    And request { firstName: 'Bob', lastName: 'Johnson', email: 'bob.johnson@example.com', department: 'Sales', salary: 55000.0 }
    When method post
    Then status 201
    * def employeeId = response.id

    Given path '/employees/' + employeeId
    And request { firstName: 'Robert', lastName: 'Johnson', email: 'robert.johnson@example.com', department: 'Sales Management', salary: 60000.0 }
    When method put
    Then status 200
    And match response.firstName == 'Robert'
    And match response.department == 'Sales Management'
    And match response.salary == 60000.0

  Scenario: Delete an employee
    Given path '/employees'
    And request { firstName: 'Alice', lastName: 'Williams', email: 'alice.williams@example.com', department: 'Marketing', salary: 48000.0 }
    When method post
    Then status 201
    * def employeeId = response.id

    Given path '/employees/' + employeeId
    When method delete
    Then status 204

    Given path '/employees/' + employeeId
    When method get
    Then status 404

  Scenario: Create multiple employees and verify count
    Given path '/employees'
    And request { firstName: 'Employee1', lastName: 'Test', email: 'emp1@test.com', department: 'IT', salary: 50000.0 }
    When method post
    Then status 201
    * def count1 = response

    Given path '/employees'
    And request { firstName: 'Employee2', lastName: 'Test', email: 'emp2@test.com', department: 'IT', salary: 52000.0 }
    When method post
    Then status 201

    Given path '/employees'
    And request { firstName: 'Employee3', lastName: 'Test', email: 'emp3@test.com', department: 'Finance', salary: 54000.0 }
    When method post
    Then status 201

    Given path '/employees'
    When method get
    Then status 200
    And match response == '#[]'
    And assert response.length >= 3

  Scenario: Verify employee data persistence
    Given path '/employees'
    And request { firstName: 'Persistent', lastName: 'Employee', email: 'persistent@test.com', department: 'Operations', salary: 49000.0 }
    When method post
    Then status 201
    * def employeeId = response.id
    * def originalSalary = response.salary

    Given path '/employees/' + employeeId
    When method get
    Then status 200
    And match response.salary == originalSalary
    And match response.email == 'persistent@test.com'

  Scenario: Test invalid email format should still create employee (no validation in API)
    Given path '/employees'
    And request { firstName: 'NoEmail', lastName: 'User', email: 'invalid-email', department: 'Testing', salary: 40000.0 }
    When method post
    Then status 201
    And match response.email == 'invalid-email'

  Scenario: Test employee creation with minimum data
    Given path '/employees'
    And request { firstName: 'Min', lastName: 'Data', email: 'min@test.com', department: 'Admin', salary: 35000.0 }
    When method post
    Then status 201
    And match response.firstName == 'Min'
    And match response.createdAt != null
    And match response.updatedAt != null

  Scenario: Test update maintains employee ID
    Given path '/employees'
    And request { firstName: 'Immutable', lastName: 'ID', email: 'immutable@test.com', department: 'Ops', salary: 47000.0 }
    When method post
    Then status 201
    * def originalId = response.id

    Given path '/employees/' + originalId
    And request { firstName: 'Updated', lastName: 'ID', email: 'updated@test.com', department: 'Ops', salary: 50000.0 }
    When method put
    Then status 200
    And match response.id == originalId
    And match response.firstName == 'Updated'
