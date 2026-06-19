# Data Processor API

This project was created as part of my backend re-entry path after a career break, focusing on modern Java backend development and Spring Boot best practices.

The goal is not only to implement CRUD functionality but also to demonstrate clean architecture, testing strategies, JPA mapping techniques, API design, and modern development tooling.

---

## Features

### Core Functionality

- CRUD operations for Data entities
- Category management
- Tag management
- Many-to-many relationship implemented using an association entity (`DataTagEntity`)
- Dynamic filtering using JPA Specifications
- Pagination and sorting
- Statistics endpoint

### Backend Design

- DTO pattern
- Service layer
- Repository layer
- Validation with Jakarta Bean Validation
- Global exception handling
- Transaction management
- MapStruct object mapping
- Spring Profiles

### API Documentation

- OpenAPI / Swagger UI

### Testing

- Unit tests with Mockito
- Repository integration tests with H2
- Controller tests with MockMvc
- Validation tests
- Exception handling tests

---

## Architecture

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Current implementation:

```text
DataController
    ↓
DataService
    ↓
DataRepository
    ↓
PostgreSQL / H2
```

---

## Technology Stack

### Backend

- Java 17
- Spring Boot 4
- Spring Data JPA
- Hibernate

### Database

- H2 Database
- PostgreSQL

### Mapping

- MapStruct

### API Documentation

- SpringDoc OpenAPI
- Swagger UI

### Testing

- JUnit 5
- Mockito
- MockMvc

### Build Tool

- Maven

### Infrastructure

- Docker
- Docker Compose

---

## Running with H2

Start the application:

```bash
mvn spring-boot:run
```

H2 Console:

```text
http://localhost:8080/h2-console
```

---

## Running with PostgreSQL

### Start PostgreSQL

```bash
docker compose up -d
```

Check running containers:

```bash
docker ps
```

### Start the application

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

### Stop PostgreSQL

```bash
docker compose down
```

### Remove PostgreSQL data

```bash
docker compose down -v
```

Warning:

`down -v` removes the database volume and all stored data.

---

## API Documentation

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

Alternative URL:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

---

## Testing

Run all tests:

```bash
mvn test
```

Run a single test class:

```bash
mvn test -Dtest=DataServiceTest
```

Run a single test method:

```bash
mvn test -Dtest=DataControllerTest#shouldReturnDataById
```

---

## Current Learning Topics Covered

### Spring Boot

- REST APIs
- DTO pattern
- Validation
- Global Exception Handling
- Profiles

### JPA / Hibernate

- Entity lifecycle
- Persistence Context
- Lazy Loading
- N+1 problem
- Entity Graph
- Specifications
- Many-to-One relationships
- Association Entity pattern
- Cascading
- Orphan Removal

### Testing

- Unit Testing
- Integration Testing
- Controller Testing
- MockMvc
- Mockito

### Infrastructure

- Docker basics
- Docker Compose
- PostgreSQL containers
- Persistent volumes

---

## Planned Improvements

- Dockerize Spring Boot application
- Testcontainers
- Spring Security
- JWT Authentication
- CI/CD pipeline
- GitHub Actions

---

## Author

Katalin Brüller

Backend re-entry project focused on modern Spring Boot development.