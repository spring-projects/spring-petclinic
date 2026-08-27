# Vaccination Management

## Overview

Implemented Vaccination Management for the Spring PetClinic application.

A Pet can have multiple Vaccinations. Each vaccination stores:

- Vaccine Name
- Vaccination Date
- Next Due Date
- Notes

Users can create and view vaccinations directly from the existing Owner/Pet screens.

## Implementation

The implementation follows the existing PetClinic architecture and conventions:

- `Vaccination` is a JPA entity under the existing `owner` domain package.
- `Pet` maintains a collection of Vaccinations.
- Vaccinations are persisted through the existing `OwnerRepository` and cascading relationship.
- `VaccinationController` follows the existing `VisitController` pattern.
- Thymeleaf is used for the vaccination form and display.
- Existing Bean Validation and Spring MVC validation mechanisms are reused.
- Database schemas were updated for H2, MySQL, and PostgreSQL.

## Validation

The following validations are implemented:

- Vaccine Name is required.
- Next Due Date cannot be before Vaccination Date.
- Entity IDs are protected from form binding.

Validation errors are displayed using the existing PetClinic form/error handling mechanism.

## Tests

Automated controller tests cover:

- Opening the vaccination form.
- Successfully creating a vaccination.
- Missing vaccine name validation.
- Invalid Next Due Date validation.

The existing application test suite also passes with the vaccination changes.

Run all tests with:

```bash
./mvnw test
