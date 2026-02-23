# Repository Guidelines

## Project Structure & Module Organization
- `src/main/java/` application source (Spring Boot).
- `src/main/resources/` templates, static assets, and config (e.g., `application.properties`).
- `src/test/java/` unit/integration tests.
- `build.gradle` / `settings.gradle` and `pom.xml` support Gradle or Maven builds.
- `docker-compose.yml` and `k8s/` provide container and Kubernetes helpers.

## Build, Test, and Development Commands
- `./mvnw spring-boot:run` runs the app with Maven.
- `./gradlew bootRun` runs the app with Gradle.
- `./mvnw test` or `./gradlew test` runs tests.
- `./mvnw spring-boot:build-image` builds a container image via Spring Boot.
- `./mvnw package -P css` regenerates CSS from `petclinic.scss` (Maven only).

## Coding Style & Naming Conventions
- Java code uses standard Spring conventions; keep package names under `org.springframework.samples.petclinic`.
- Prefer 4-space indentation in Java and consistent formatting in HTML/Thymeleaf templates.
- Keep resource keys in `src/main/resources/messages/` and reference them via `#{key}`.
- Use existing naming patterns (e.g., `*Controller`, `*Service`, `*Repository`, `*Tests`).

## Testing Guidelines
- Tests live under `src/test/java/` and use JUnit (Spring Boot test support).
- Integration tests exist for MySQL/PostgreSQL; run with the standard test tasks above.
- Name tests with `*Tests` or `*IT` to match existing conventions.

## Commit & Pull Request Guidelines
- Commits must include a `Signed-off-by` trailer (Developer Certificate of Origin).
- Keep commit messages concise and action-oriented (e.g., “Fix visit validation”).
- PRs should describe behavior changes, list test coverage, and link related issues.

## Configuration Tips
- Default DB is in-memory H2; switch via profiles:
  - `spring.profiles.active=mysql`
  - `spring.profiles.active=postgres`
- MySQL/Postgres can be started via `docker compose up mysql|postgres`.
