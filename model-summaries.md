# 📦 Model Package Summaries

This document explains the core domain classes in the `org.springframework.samples.petclinic.model` package. These classes are the foundation of the PetClinic data model and are extended by other parts of the application.

---

## 🧱 BaseEntity.java

`BaseEntity` is a superclass for all entities that need a unique identifier (`id`). It uses JPA annotations to mark the `id` as the primary key and auto-generates its value using the `IDENTITY` strategy. This means the database will assign the ID when the object is saved.

The class implements `Serializable`, which allows objects to be converted into a byte stream for storage or transmission. It also includes a method `isNew()` that returns `true` if the entity hasn't been saved yet (i.e., its `id` is `null`). This is useful for checking whether an object is new or already exists in the database.

---

## 🏷️ NamedEntity.java

`NamedEntity` extends `BaseEntity` and adds a `name` field. This class is used for entities that need both an ID and a name, such as `PetType` or `Specialty`.

The `name` field is annotated with `@NotBlank`, which ensures that it cannot be empty. The `@Column(name = "name")` annotation maps it to the corresponding column in the database. The `toString()` method returns the name if it's available, or `<null>` if it's not set, which helps with debugging and logging.

---

## 👤 Person.java

`Person` is another subclass of `BaseEntity`, and it adds two fields: `firstName` and `lastName`. These fields are also annotated with `@NotBlank` and mapped to database columns using `@Column`.

This class represents a generic person and is extended by more specific classes like `Owner` and `Vet`. By centralizing common fields here, the project avoids duplication and keeps the codebase clean and maintainable.

---

## 📦 package-info.java

This file contains a package-level annotation `@NullMarked`, which indicates that all types in this package are non-null by default unless explicitly marked as `@Nullable`. This improves null-safety and helps developers avoid null pointer exceptions.

It also includes a brief comment explaining that the classes in this package represent utilities used by the domain. While it doesn't contain executable code, it plays an important role in documentation and type safety.
