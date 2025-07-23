# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spring PetClinic is a canonical Spring Boot 3.5.0 sample application demonstrating modern Java web development. It's a veterinary clinic management system requiring Java 17+ with dual build system support (Maven primary, Gradle alternative).

## Common Commands

### Build and Run
```bash
# Build and package
./mvnw package

# Run application 
./mvnw spring-boot:run

# Run with specific database profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql

# Full verification (tests, checkstyle, formatting)
./mvnw verify

# Generate CSS from SCSS (requires -P css profile)
./mvnw package -P css
```

### Testing
```bash
# Run all tests
./mvnw test

# Integration test applications for development:
# - PetClinicIntegrationTests.main() - H2 with DevTools
# - MySqlTestApplication.main() - MySQL with Testcontainers  
# - PostgresIntegrationTests.main() - PostgreSQL with Docker Compose
```

### Database Services
```bash
# Start MySQL
docker compose up mysql

# Start PostgreSQL
docker compose up postgres
```

## Architecture

### Domain Structure
- **owner/**: Owner, Pet, Visit entities with controllers/repositories
- **vet/**: Veterinarian and Specialty entities
- **system/**: Configuration classes and system controllers
- **model/**: Base entity classes (BaseEntity, NamedEntity, Person)

### Technology Stack
- **Spring Boot 3.5.0** with Spring MVC and Spring Data JPA
- **Thymeleaf** templating with Bootstrap 5.3.6 frontend
- **Database support**: H2 (default), MySQL 9.2, PostgreSQL 17.5
- **Caching**: Caffeine with JSR-107 API
- **Testing**: JUnit 5, Testcontainers, Spring Boot Test

### Key Patterns
- Layered architecture: Controller → Repository → Entity
- Domain-driven design with clear package separation
- Spring profiles for environment-specific configuration (`mysql`, `postgres`)
- Comprehensive validation with custom validators (e.g., PetValidator)

## Database Configuration

- **H2 Console**: http://localhost:8080/h2-console (development)
- **MySQL**: localhost:3306/petclinic (user: petclinic/petclinic)
- **PostgreSQL**: localhost:5432/petclinic (user: petclinic/petclinic)
- SQL scripts in `src/main/resources/db/{h2,mysql,postgres}/`

## Development Notes

- **Code formatting**: Spring Java Format plugin enforced via Maven
- **Quality checks**: Checkstyle rules in `src/checkstyle/`, NoHTTP plugin prevents insecure URLs
- **CSS development**: SCSS files in `src/main/scss/`, compile with `-P css` profile
- **Application URL**: http://localhost:8080/
- **Actuator endpoints**: http://localhost:8080/actuator (all endpoints exposed)
- **Native compilation**: GraalVM support via PetClinicRuntimeHints
- **CI/CD**: GitHub Actions for both Maven and Gradle builds

## Test Strategy

- Unit tests per domain package
- Integration tests with full application context
- Database-specific tests using Testcontainers and Docker Compose
- JMeter performance test plan in `src/test/jmeter/`
- Special test main() methods for rapid development feedback