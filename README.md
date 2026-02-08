Prerequisites
Java 17+
Docker (for PostgreSQL)
Maven

1. Start PostgreSQL

   docker run --name petclinic-ff \
   -e POSTGRES_DB=petclinic \
   -e POSTGRES_USER=petclinic \
   -e POSTGRES_PASSWORD=petclinic \
   -p 5432:5432 \
   -d postgres:15

2. Run Application
   ./mvnw spring-boot:run -Dspring.profiles.active=postgres


URLs:
    App: http://localhost:8080
    Flags API: http://localhost:8080/api/flags
    H2 Console: http://localhost:8080/h2-console
    Database: localhost:5432/petclinic (user: petclinic, pass: petclinic)

Feature Flags Implemented
    Flag Key	        Controls	        Controller Method	                    Strategy Examples
    add-pet	            Add New Pet form	PetController.initNewPetForm()	        Global OFF/ON, Percentage
    add-visit	        Add Visit form	    VisitController.processNewVisit()	    Percentage rollout (50%)
    owner-search	    Owner search	    OwnerController.processFindForm()	    Blacklist/Whitelist users

Behavior when OFF: Returns 403 Forbidden → redirects to /oups error page.

Feature Flag Strategies (Advanced)
    Strategy	        Configuration	                    Logic
    GLOBAL	            {"enabled": true}	                Everyone ON/OFF
    BLACKLIST	        {"users":["test@example.com"]}	    Blocks listed users
    WHITELIST	        {"users":["admin@company.com"]}	    Only listed users
    PERCENTAGE	        {"percentage":25}	                25% of users randomly

Feature Flag Management API
    GET    /api/flags                    # List all flags
    GET    /api/flags/{key}              # Get specific flag
    POST   /api/flags                    # Create flag
    PUT    /api/flags/{key}              # Update flag  
    DELETE /api/flags/{key}              # Delete flag

Example API Calls
1. Global OFF (blocks everyone):
curl -X POST http://localhost:8080/api/flags \
-H "Content-Type: application/json" \
-d '{
"flagKey": "add-pet",
"name": "Add New Pet",
"enabled": false,
"strategyType": "GLOBAL"
}'

2. Percentage Rollout (50% users):
curl -X POST http://localhost:8080/api/flags \
-H "Content-Type: application/json" \
-d '{
"flagKey": "add-visit",
"name": "Add Visit",
"enabled": true,
"strategyType": "PERCENTAGE",
"strategyValue": "{\"percentage\":50}"
}'

3. Blacklist specific user:
curl -X POST http://localhost:8080/api/flags \
-H "Content-Type: application/json" \
-d '{
"flagKey": "owner-search",
"name": "Owner Search",
"enabled": true,
"strategyType": "BLACKLIST",
"strategyValue": "{\"users\":[\"test@example.com\"]}"
}'

**Test with user email: Add ?email=test@example.com to flagged URLs.**


**Architecture Overview**

    ┌─────────────────┐    ┌──────────────────┐    ┌─────────────┐
    │   PetClinic     │◄──►│FeatureFlagService│◄──►│ PostgreSQL  │
    │   Controllers   │    │  + Strategies    │    │  feature_   │
    │                 │    │                  │    │  flags table│
    └─────────────────┘    └──────────────────┘    └─────────────┘
    │                      ▲
    └──────────┬───────────┘
               │
    ┌─────────────────┐
    │  @FeatureFlag   │  ← Custom Annotation + AOP
    │    Aspect       │
    └─────────────────┘


**Key Implementation Features**
    Custom-built (No FF4J/Togglz)
    Database persistence (survives restarts)
    4 Strategies: Global/Blacklist/Whitelist/Percentage
    Custom Annotation @FeatureFlag("add-pet") + AOP
    Helper function: featureFlagService.isFeatureEnabled(flagKey, userEmail)
    Edge cases: Fail-open, JSON parsing errors, missing flags default ON
    No authentication on flag API (per requirements)

**Code Locations**
    src/main/java/org/springframework/samples/petclinic/model/
    ├── FeatureFlag.java              # Entity 

    src/main/java/org/springframework/samples/petclinic/system/
    ├── FeatureFlag.java              #  @FeatureFlag annotation

    src/main/java/org/springframework/samples/petclinic/service/
    ├── FeatureFlagService.java       # Core logic + strategies

    src/main/java/org/springframework/samples/petclinic/repository/
    ├── FeatureFlagRepository.java    # JPA

    src/main/java/org/springframework/samples/petclinic/controller/
    ├── FeatureFlagController.java    # REST API

    src/main/java/org/springframework/samples/petclinic/aop/
    └── FeatureFlagAspect.java        # AOP interceptor

    Controllers with flags:
    ├── PetController.java           # @FeatureFlag("add-pet")
    ├── VisitController.java         # @FeatureFlag("add-visit")  
    └── OwnerController.java         # @FeatureFlag("owner-search")

**Demo Script**
    1. App running normally → All 3 features work
    2. Create Global OFF flag → Add Pet → 403 Forbidden
    3. Percentage flag → Add Visit works ~50% time (random)
    4. Blacklist → Owner search blocked for test@example.com
    5. Database → Flags persist after restart
    6. CRUD → Create/Update/Delete via API

**Troubleshooting**

    Issue	                Solution
    AOP not working	        Add @EnableAspectJAutoProxy to PetClinicApplication.java
    JSON parsing fails	    Check strategyValue format
    Flags not persisting	Verify Postgres connection
    Package not found	    Use org.springframework.samples.petclinic.system

**Original Documentation**
    For original PetClinic features, see Spring Petclinic.

🎥 Loom Video Walkthrough: [Insert Loom Link Here]

✨ Fully functional with advanced feature flag strategies, custom annotation, and production-ready code!
