# ==========================================
# WORKSPACE RULES: Spring PetClinic (16k Context Optimization)
# ==========================================

[Environment & Constraints]
- Context Window Limit: Strict 16,000 tokens.
- Language/Framework: Java 17+, Spring Boot, Thymeleaf, Spring Data JPA.

[Project Architecture Map]
Base Java Path: src/main/java/org/springframework/samples/petclinic/
- owner/   : Owner, Pet, Visit (Entities, Repositories, Controllers)
- vet/     : Veterinarian (Entities, Specialities, Controllers)
- system/  : WelcomeController, Cache, Error handling
- model/   : Base classes (BaseEntity, NamedEntity, Person)

Base UI Path: src/main/resources/
- templates/ : Thymeleaf HTML views (layout, owners, vets)
- static/    : CSS, JS, Images

[Strict Tool-Use Rules for Context Saving]
1. DO NOT read multiple files or execute recursive file listings.
2. If you need to investigate a feature, open ONLY the single most relevant file at a time.
3. Trust the 'Project Architecture Map' above instead of searching the entire project tree.
4. When writing code, output only the modified or relevant parts to save tokens. Do not rewrite whole files if not necessary.
5. If you run out of context, stop and ask the user for guidance immediately.
