# Java Files Summary

This document lists the main Java Files in the Spring PetClinic project along with a short description of what each file does.

--------

### 1. PetCLinicApplication.java
**Path:** `src/main/java/org/springframework/samples/petclinic/`
This is the main entry point of the SpringBoot Application. It starts the embedded server and loads the PetClinic context.

--------

### 3. OwnerController.java
**Path:** `src/main/java/org/springframework/samples/petclinic/owner/`
Handles all web requests related to pet owners, like viewing, adding, and updatingowner information.

--------

### 4. PetController.java
**Path:** `src/main/java/org/springframework/samples/petclinic/owner/`
Manages pet-related actions - adding new pets, editing pet details, and linking pets to their owners.

--------

### 4. VetController.java
**Path:** `src/main/java/org/springframework/samples/petclinic/vet/`  
Handles veterinarian data — listing vets and showing their specialties.

--------

### 5. VisitController.java
**Path:** `src/main/java/org/springframework/samples/petclinic/owner/`  
Manages pet visit records — adding or viewing a pet’s medical visits.

--------

### 6. Owner.java
**Path:** `src/main/java/org/springframework/samples/petclinic/owner/`  
Represents the Owner entity — includes details like name, address, city, and pets list.

--------

### 7. Pet.java
**Path:** `src/main/java/org/springframework/samples/petclinic/owner/`  
Represents the Pet entity — includes name, birth date, type, pet id and date in ascending order.

--------

### 8. Vet.java
**Path:** `src/main/java/org/springframework/samples/petclinic/vet/`  
Represents the Vet entity — contains information about veterinarians and their specialties.

--------

### 9. Visit.java
**Path:** `src/main/java/org/springframework/samples/petclinic/owner/`  
Represents a Visit entity — stores visit date, description, and associated pet.

--------

### 10. PetClinicApplicationTests.java
**Path:** `src/test/java/org/springframework/samples/petclinic/`  
Registers runtime hints for native image builds, ensuring required resources and classes are included during AOT compilation.

--------

### Notes
-> All controllers handle web requests and return HTML views using Thymeleaf templates.
-> Entities (`Owner`, `Pet`, `Vet`, `Visit`) represent database tables managed by Spring Data JPA.
-> The application uses MVC (Model-View-Controller) design pattern.
