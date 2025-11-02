# Spring PetClinic, First 20 Java Files Summary

This document provides a brief description of the first 20 Java files in the repository.

| #  | File                         | Description                                                                                                                                                  |
|----|------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1  | `VetController.java`         | Spring MVC controller that handles displaying and paginating the list of veterinarians, supporting both HTML views and JSON/XML responses.                   |
| 2  | `PetClinicApplication.java`  | Main class that bootstraps and runs the Spring Boot application.                                                                                             |
| 3  | `PetClinicRuntimeHints.java` | Registers runtime hints for Spring Boot native compilation, including resources and serializable classes.                                                    |
| 4  | `BaseEntity.java`            | Abstract base class providing an auto-generated `id` property and helper methods for all entities.                                                           |
| 5  | `NamedEntity.java`           | Extends `BaseEntity` by adding a `name` property with validation and column mapping for reusable entity naming.                                              |
| 6  | `Vet.java`                   | Entity representing a veterinarian, extending `Person`, with a many-to-many relationship to specialties and helper methods for managing them.                |
| 7  | `Person.java`                | Abstract class representing a person with `firstName` and `lastName` properties for shared inheritance.                                                      |
| 8  | `Owner.java`                 | Entity representing a pet owner, including address, contact info and a one-to-many relationship with their pets.                                             |
| 9  | `OwnerController.java`       | Spring MVC controller that handles creating, updating and viewing pet owners, including paginated search and form validation logic.                          |
| 10 | `OwnerRepository.java`       | Spring Data JPA repository interface for `Owner` entities, providing query methods for searching by last name (with pagination) and retrieving owners by ID. |
| 11 | `WebConfiguration.java`      | Configures internationalization (i18n) support, managing default locale, session-based locale storage and URL-based language switching.                      |
| 12 | `Pet.java`                   | Entity representing a pet, including birth date, type and a one-to-many relationship with visits.                                                            |
| 13 | `PetController.java`         | Spring MVC controller managing creation, update and retrieval of pets for a specific owner, including validation and type population.                        |
| 14 | `PetType.java`               | Entity representing a pet type (e.g., Cat, Dog, Hamster), extending `NamedEntity` with an ID and name.                                                       |
| 15 | `PetTypeFormatter.java`      | Spring MVC component that formats and parses `PetType` objects for display and binding in forms.                                                             |
| 16 | `PetTypeRepository.java`     | Repository interface for `PetType` entities, providing database access and a method to retrieve all pet types ordered by name.                               |
| 17 | `PetValidator.java`          | Validator for `Pet` objects, enforcing that new pets have a name, type and birth date before being saved or updated.                                         |
| 18 | `Visit.java`                 | Domain object representing a pet visit, storing the visit date and a description of the visit.                                                               |
| 19 | `VisitController.java`       | Handles creating and processing pet visits, linking each visit to the correct pet and owner.                                                                 |
| 20 | `CacheConfiguration.java`    | Configures application caching using JCache, enabling statistics and creating a cache for vets.                                                              |
