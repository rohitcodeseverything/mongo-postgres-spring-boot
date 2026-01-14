# Multi-Database Spring Boot Application

A Spring Boot application demonstrating integration with both **MongoDB** (for Employee Management) and **PostgreSQL** (for Admin Management) with comprehensive testing strategies.

## Project Structure

```
mongo-postgres-spring-boot/
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── Application.java
│   │   │   ├── employee/
│   │   │   │   ├── config/
│   │   │   │   │   └── MongoConfig.java
│   │   │   │   ├── controller/
│   │   │   │   │   └── EmployeeController.java
│   │   │   │   ├── entity/
│   │   │   │   │   └── Employee.java
│   │   │   │   └── repository/
│   │   │   │       └── EmployeeRepository.java
│   │   │   └── admin/
│   │   │       ├── config/
│   │   │       │   └── JpaConfig.java
│   │   │       ├── controller/
│   │   │       │   └── AdminController.java
│   │   │       ├── entity/
│   │   │       │   └── Admin.java
│   │   │       └── repository/
│   │   │           └── AdminRepository.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       ├── java/com/example/
│       │   ├── employee/
│       │   │   └── EmployeeIntegrationTest.java
│       │   └── admin/
│       │       └── AdminIntegrationTest.java
│       └── resources/
│           ├── application-test.yml
│           └── application-test-postgres.yml
├── pom.xml
└── README.md
```

## Features

### 1. Employee Management (EMC) - MongoDB
- **Technology**: Spring Data MongoDB
- **Database**: MongoDB
- **Testing**: Flapdoodle Embedded MongoDB with @SpringBootTest
- **Endpoints**:
  - `POST /api/employees` - Create employee
  - `GET /api/employees` - Get all employees
  - `GET /api/employees/{id}` - Get employee by ID
  - `PUT /api/employees/{id}` - Update employee
  - `DELETE /api/employees/{id}` - Delete employee

### 2. Admin Management (AMC) - PostgreSQL
- **Technology**: Spring Data JPA
- **Database**: PostgreSQL
- **Testing**: Testcontainers for PostgreSQL integration tests
- **Endpoints**:
  - `POST /api/admins` - Create admin
  - `GET /api/admins` - Get all admins
  - `GET /api/admins/{id}` - Get admin by ID
  - `PUT /api/admins/{id}` - Update admin
  - `DELETE /api/admins/{id}` - Delete admin

## Prerequisites

- Java 17 or later
- Maven 3.6 or later
- Docker (for Testcontainers in PostgreSQL tests)

## Getting Started

### 1. Clone and Build

```bash
git clone <repository-url>
cd mongo-postgres-spring-boot
mvn clean install
```

### 2. Running the Application

For development, you need both MongoDB and PostgreSQL running locally:

```bash
# Start MongoDB
mongod

# Start PostgreSQL
psql -U postgres
```

Then run the application:

```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

## Testing

### Employee CRUD Tests (Flapdoodle)

These tests use Flapdoodle to run an embedded MongoDB instance:

```bash
mvn test -Dtest=EmployeeIntegrationTest
```

**Test Coverage:**
- ✅ Create employee
- ✅ Retrieve all employees
- ✅ Retrieve employee by ID
- ✅ Retrieve employee by email
- ✅ Update employee
- ✅ Delete employee
- ✅ Non-existent employee handling
- ✅ Employee count verification
- ✅ Employee existence check
- ✅ Delete all employees

### Admin CRUD Tests (Testcontainers)

These tests use Testcontainers to run PostgreSQL in a Docker container:

```bash
mvn test -Dtest=AdminIntegrationTest
```

**Test Coverage:**
- ✅ Create admin
- ✅ Retrieve all admins
- ✅ Retrieve admin by ID
- ✅ Retrieve admin by username
- ✅ Retrieve admin by email
- ✅ Update admin
- ✅ Delete admin
- ✅ Non-existent admin handling
- ✅ Admin count verification
- ✅ Admin existence check
- ✅ Delete all admins
- ✅ Container verification
- ✅ Admin deactivation

### Run All Tests

```bash
mvn test
```

## Configuration

### Application Properties (application.yml)

**MongoDB Configuration:**
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/employee_db
      auto-index-creation: true
```

**PostgreSQL Configuration:**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/admin_db
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
```

### Test Profiles

**test Profile (application-test.yml):**
- Uses Flapdoodle embedded MongoDB
- Uses H2 for PostgreSQL tests (in-memory database)

**test-postgres Profile (application-test-postgres.yml):**
- Uses Testcontainers PostgreSQL
- Disables MongoDB

## API Examples

### Employee Endpoints

```bash
# Create employee
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "department": "Engineering",
    "salary": 50000
  }'

# Get all employees
curl http://localhost:8080/api/employees

# Get employee by ID
curl http://localhost:8080/api/employees/{id}

# Update employee
curl -X PUT http://localhost:8080/api/employees/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jane",
    "department": "HR",
    "salary": 55000
  }'

# Delete employee
curl -X DELETE http://localhost:8080/api/employees/{id}
```

### Admin Endpoints

```bash
# Create admin
curl -X POST http://localhost:8080/api/admins \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin01",
    "email": "admin01@example.com",
    "password": "password123",
    "fullName": "John Admin",
    "role": "SUPER_ADMIN"
  }'

# Get all admins
curl http://localhost:8080/api/admins

# Get admin by ID
curl http://localhost:8080/api/admins/{id}

# Update admin
curl -X PUT http://localhost:8080/api/admins/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "John Updated",
    "role": "ADMIN"
  }'

# Delete admin
curl -X DELETE http://localhost:8080/api/admins/{id}
```

## Dependencies

### Core Dependencies
- Spring Boot 3.2.0
- Spring Data MongoDB
- Spring Data JPA
- Spring Web

### Testing Dependencies
- JUnit 5
- AssertJ
- Flapdoodle Embed MongoDB 4.0.0
- Testcontainers 1.19.4

## Key Design Decisions

1. **Separate Repositories**: Employee and Admin use separate database implementations (MongoDB vs PostgreSQL)
2. **Test Isolation**: Each test suite uses its own embedded/containerized database instance
3. **Active Profiles**: Different profiles for different test scenarios
4. **Lombok**: Reduces boilerplate code for entities
5. **Temporal Fields**: Both entities include `createdAt` and `updatedAt` fields for audit trails

## Running Specific Tests

```bash
# Run only Employee tests
mvn test -Dtest=EmployeeIntegrationTest

# Run only Admin tests
mvn test -Dtest=AdminIntegrationTest

# Run specific test method
mvn test -Dtest=EmployeeIntegrationTest#testCreateEmployee

# Run with coverage
mvn clean test jacoco:report
```

## Troubleshooting

### MongoDB Connection Issues
- Ensure MongoDB is running on `localhost:27017`
- Check MongoDB logs for connection errors

### PostgreSQL Connection Issues
- Ensure PostgreSQL is running on `localhost:5432`
- Verify credentials in `application.yml`
- Create `admin_db` database: `createdb admin_db`

### Testcontainers Issues
- Ensure Docker is installed and running
- Check Docker daemon logs
- Verify sufficient disk space for container images

### Flapdoodle Issues
- Clear Maven cache: `mvn clean`
- Ensure Java 17 or later is installed
- Check internet connection for downloading embedded MongoDB

## Next Steps

1. Add authentication/authorization
2. Implement service layer
3. Add request validation
4. Create DTOs for API responses
5. Add exception handling
6. Implement logging
7. Add API documentation (Swagger/OpenAPI)

## License

This project is provided as-is for educational and development purposes.
