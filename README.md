# Feature Flag System - Spring PetClinic Integration

## Overview
This project adds a custom-built Feature Flag system to the Spring PetClinic application without using any third‑party feature flag libraries. The system allows runtime control of selected features using database-driven flags and an AOP-based custom annotation.


Features can be enabled/disabled globally and also support whitelist, blacklist, and percentage rollout strategies.

---

## Tech Stack


- Java 17+
- Spring Boot 4.0.1 (Default version of Spring PetClinic)
- Spring AOP
- Spring Data JPA
- MySQL
- Maven


---


## How To Run Locally

### Clone Repo

You first need to clone the project locally:

```bash
git clone https://github.com/spring-projects/spring-petclinic.git
cd spring-petclinic
```

## Database configuration

In its default configuration, Petclinic uses an in-memory database (H2) which
gets populated at startup with data.

A similar setup is provided for MySQL and PostgreSQL if a persistent database configuration is needed. Note that whenever the database type changes, the app needs to run with a different profile: `spring.profiles.active=mysql` for MySQL or `spring.profiles.active=postgres` for PostgreSQL. See the [Spring Boot documentation](https://docs.spring.io/spring-boot/how-to/properties-and-configuration.html#howto.properties-and-configuration.set-active-spring-profiles) for more detail on how to set the active profile.

I have used MySQL 8.0.34 for development and testing.
For which I have added the following configuration to `src/main/resources/application-mysql.properties`:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Before running the application with MySQL, make sure to create a database named `petclinic` if required.

### Build & Run

You can start the application using maven on the command-line as follows:

```bash
./mvnw spring-boot:run
```
If you are using MySQL, you can start the application with the MySQL profile:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
```
You can then access the Petclinic at <http://localhost:8080/>.

---


## Feature Flags Implemented


| Feature      | Flag Name    | Location                                 |
|--------------|--------------|------------------------------------------|
| Add Pet      | ADD_PET      | PetController POST processCreationForm   |
| Add Visit    | ADD_VISIT    | VisitController POST processNewVisitForm |
| Owner Search | OWNER_SEARCH | OwnerController GET processFindForm      |


Each feature is protected using:
@FeatureSwitch("FLAG_NAME")

---


## Feature Flag Capabilities


### Global Enable/Disable
Turns feature fully on/off.


### Whitelist
Specific users always enabled.


### Blacklist
Specific users always blocked.


### Percentage Rollout
Deterministic hash-based rollout.


---


## API — Feature Flag Management


Base Path:
`/api/flags`

### Create Flag


POST `/api/flags`

`{ "flagName": "ADD_PET", "flagEnabled": true, "rolloutPercentage": 100 }`

Returns **201 CREATED**


---


### List Flags


GET `/api/flags`


---

### List Flag based on flag name

GET `/api/flags/{name}`


GET `/api/flags`


---

### Delete Flag


DELETE `/api/flags/{name}`


Returns **204 NO CONTENT**


---


## Design Decisions


- Built custom annotation + AOP instead of libraries (per requirement)
- DB-backed persistence
- Deterministic rollout using hash bucket
- Default behavior = disabled if flag missing
- No authentication on flag APIs (as per requirement)


---

## Edge Cases Handled


- Missing flag is treated as feature disabled
- 0% rollout is treated as always blocked
- 100% rollout is always enabled
- Blacklist overrides whitelist

---

## Package Structure (New Code)

The new code is placed in a separate package to maintain modularity and separation of concerns:
- `org.springframework.examples.petclinic.featureflags` — Core feature flag system (annotation, aspect, service, repository)

## Credits

Base project forked from:

Spring PetClinic — https://github.com/spring-projects/spring-petclinic

Feature Flag system implementation added as part of assignment work.
