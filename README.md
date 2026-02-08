# Feature Flag Enabled Spring PetClinic

This project is an extension of the official **Spring PetClinic** application with a **custom-built Feature Flag service** implemented from scratch (without using libraries like FF4J or Togglz).

The feature flag system allows enabling/disabling application features dynamically using database-driven flags that persist across restarts.

---

## How to Run the Application

### Prerequisites
- Java 17+
- Maven 3.8+
- Docker & Docker Compose

### Steps
1. **Clone the repository**
   ```bash
   git clone https://github.com/XTiNCT-7/spring-petclinic.git
   cd spring-petclinic
   ```

2. **Start MySQL using Docker**
   ```bash
   docker-compose up mysql
   ```

3. **Build and run the application**
   ```bash
   mvn clean spring-boot:run
   ```

4. Access the application:
   - Application UI: `http://localhost:8080`
   - Feature Flag APIs: `http://localhost:8080/feature-flags`

---

## Assumptions & Design Decisions

- Feature flags are **stored in the database** and persist across application restarts.
- No authentication is applied to feature flag management APIs (as per requirement).
- Feature flag evaluation is **centralized in a helper service** so it can be reused across controllers, services, and views.
- A **custom annotation (`@FeatureToggle`)** is used to guard controller endpoints.
- Thymeleaf views are conditionally rendered using model attributes derived from feature flag checks.
- Feature flag behavior supports more than boolean enable/disable:
  - Global enable/disable
  - Whitelist-based access
  - Blacklist-based restriction
  - Percentage rollout (future-safe design)

---

## Feature Flags Implemented

| Feature Flag Key | Type | Controls | Implementation Location |
|------------------|------|----------|--------------------------|
| `ADD_NEW_PET` | SIMPLE | Enables adding a new pet to an owner | `PetController`, `ownerDetails.html` |
| `ADD_VISIT` | SIMPLE | Enables adding a visit for a pet | `VisitController`, `ownerDetails.html` |
| `OWNER_SEARCH` | SIMPLE | Enables owner search functionality | `OwnerController`, `findOwners.html` |

### Example
- If `ADD_NEW_PET` is disabled:
  - The "Add New Pet" button is hidden in the UI
  - Direct access to `/owners/{id}/pets/new` is blocked using `@FeatureToggle`

---

## Feature Flag Management APIs

Base Path: `/feature-flags`

### Create Feature Flag
```http
POST /feature-flags
```

**Request Body**
```json
{
  "flagKey": "ADD_NEW_PET",
  "description": "Controls whether users can add new pets",
  "enabled": true,
  "flagType": "SIMPLE"
}
```

---

### Get All Feature Flags
```http
GET /feature-flags
```

---

### Get Feature Flag by Key
```http
GET /feature-flags/{flagKey}
```

---

### Update Feature Flag
```http
PUT /feature-flags/{id}
```

---

### Delete Feature Flag
```http
DELETE /feature-flags/{id}
```

---

## Custom Annotation Usage

```java
@FeatureToggle(
    key = "OWNER_SEARCH",
    disabledMessage = "Owner search is restricted",
    disabledRedirect = "/owners/find"
)
```

This annotation prevents access to the controller method when the feature is disabled and optionally redirects the user.

---

## References
- Spring PetClinic: https://github.com/spring-projects/spring-petclinic

---


