Feature: Admin API CRUD Operations

  Background:
    * url appUrl
    * header Content-Type = 'application/json'

  Scenario: Create a new admin
    Given path '/admins'
    And request { username: 'admin1', email: 'admin1@example.com', password: 'password123', fullName: 'Admin One', role: 'ADMIN' }
    When method post
    Then status 201
    And match response.username == 'admin1'
    And match response.email == 'admin1@example.com'
    And match response.fullName == 'Admin One'
    And match response.role == 'ADMIN'
    And match response.active == true
    And match response.id != null
    And match response.createdAt != null
    And match response.updatedAt != null
    * def adminId = response.id

  Scenario: Get all admins
    Given path '/admins'
    When method get
    Then status 200
    And match response == '#[]'

  Scenario: Create admin and retrieve by ID
    Given path '/admins'
    And request { username: 'admin2', email: 'admin2@example.com', password: 'securepass456', fullName: 'Admin Two', role: 'SUPERADMIN' }
    When method post
    Then status 201
    * def adminId = response.id

    Given path '/admins/' + adminId
    When method get
    Then status 200
    And match response.username == 'admin2'
    And match response.email == 'admin2@example.com'
    And match response.fullName == 'Admin Two'
    And match response.role == 'SUPERADMIN'
    And match response.id == adminId

  Scenario: Update an admin
    Given path '/admins'
    And request { username: 'admin3', email: 'admin3@example.com', password: 'initialpass', fullName: 'Admin Three', role: 'ADMIN' }
    When method post
    Then status 201
    * def adminId = response.id

    Given path '/admins/' + adminId
    And request { username: 'admin3_updated', email: 'admin3_updated@example.com', password: 'newpass789', fullName: 'Admin Three Updated', role: 'MANAGER', active: true }
    When method put
    Then status 200
    And match response.username == 'admin3_updated'
    And match response.email == 'admin3_updated@example.com'
    And match response.fullName == 'Admin Three Updated'
    And match response.role == 'MANAGER'
    And match response.id == adminId

  Scenario: Delete an admin
    Given path '/admins'
    And request { username: 'admin4', email: 'admin4@example.com', password: 'temppass', fullName: 'Admin Four', role: 'VIEWER' }
    When method post
    Then status 201
    * def adminId = response.id

    Given path '/admins/' + adminId
    When method delete
    Then status 204

    Given path '/admins/' + adminId
    When method get
    Then status 404

  Scenario: Create multiple admins and verify count
    Given path '/admins'
    And request { username: 'bulk_admin1', email: 'bulk1@test.com', password: 'pass123', fullName: 'Bulk Admin 1', role: 'ADMIN' }
    When method post
    Then status 201

    Given path '/admins'
    And request { username: 'bulk_admin2', email: 'bulk2@test.com', password: 'pass456', fullName: 'Bulk Admin 2', role: 'ADMIN' }
    When method post
    Then status 201

    Given path '/admins'
    And request { username: 'bulk_admin3', email: 'bulk3@test.com', password: 'pass789', fullName: 'Bulk Admin 3', role: 'MANAGER' }
    When method post
    Then status 201

    Given path '/admins'
    When method get
    Then status 200
    And match response == '#[]'
    And assert response.length >= 3

  Scenario: Verify admin data persistence
    Given path '/admins'
    And request { username: 'persist_admin', email: 'persist@test.com', password: 'persistpass', fullName: 'Persistent Admin', role: 'ADMIN' }
    When method post
    Then status 201
    * def adminId = response.id
    * def originalRole = response.role
    * def originalFullName = response.fullName

    Given path '/admins/' + adminId
    When method get
    Then status 200
    And match response.role == originalRole
    And match response.fullName == originalFullName
    And match response.email == 'persist@test.com'
    And match response.active == true

  Scenario: Test admin creation with all fields
    Given path '/admins'
    And request { username: 'fulldata_admin', email: 'fulldata@test.com', password: 'fullpass123', fullName: 'Full Data Admin', role: 'SUPERADMIN' }
    When method post
    Then status 201
    And match response.username == 'fulldata_admin'
    And match response.email == 'fulldata@test.com'
    And match response.fullName == 'Full Data Admin'
    And match response.role == 'SUPERADMIN'
    And match response.active == true
    And match response.createdAt != null
    And match response.updatedAt != null

  Scenario: Test update maintains admin ID
    Given path '/admins'
    And request { username: 'immutable_id_admin', email: 'immutable@test.com', password: 'immutablepass', fullName: 'Immutable ID Admin', role: 'VIEWER' }
    When method post
    Then status 201
    * def originalId = response.id

    Given path '/admins/' + originalId
    And request { username: 'immutable_id_admin', email: 'immutable_updated@test.com', password: 'newimmutablepass', fullName: 'Updated Immutable Admin', role: 'ADMIN', active: true }
    When method put
    Then status 200
    And match response.id == originalId
    And match response.fullName == 'Updated Immutable Admin'

  Scenario: Verify active status is set to true on creation
    Given path '/admins'
    And request { username: 'active_status_admin', email: 'active@test.com', password: 'activepass', fullName: 'Active Status Admin', role: 'ADMIN' }
    When method post
    Then status 201
    And match response.active == true

  Scenario: Test admin deactivation
    Given path '/admins'
    And request { username: 'deactivate_admin', email: 'deactivate@test.com', password: 'deactpass', fullName: 'Deactivate Admin', role: 'ADMIN' }
    When method post
    Then status 201
    * def adminId = response.id

    Given path '/admins/' + adminId
    And request { username: 'deactivate_admin', email: 'deactivate@test.com', password: 'deactpass', fullName: 'Deactivate Admin', role: 'ADMIN', active: false }
    When method put
    Then status 200
    And match response.active == false

    Given path '/admins/' + adminId
    When method get
    Then status 200
    And match response.active == false
