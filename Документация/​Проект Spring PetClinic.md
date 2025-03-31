Проект **Spring PetClinic** — это образец веб-приложения, демонстрирующий использование Spring Framework для создания информационной системы ветеринарной клиники. В этом проекте определены следующие основные модели:

1. **Owner** (Владелец)
2. **Pet** (Питомец)
3. **PetType** (Тип питомца)
4. **Visit** (Визит)
5. **Vet** (Ветеринар)
6. **Specialty** (Специализация)

Рассмотрим каждую из этих моделей более подробно.

### 1. Owner (Владелец)

Класс `Owner` представляет владельца одного или нескольких питомцев. Он наследует свойства от класса `Person` и добавляет специфичные для владельца атрибуты.

**Код класса:**


```java
@Entity
@Table(name = "owners")
public class Owner extends Person {

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "telephone")
    private String telephone;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private Set<Pet> pets = new HashSet<>();

    // Геттеры и сеттеры
}
```


**Описание полей:**

- `address`: Адрес владельца.
- `city`: Город проживания владельца.
- `telephone`: Контактный телефон владельца.
- `pets`: Набор питомцев, принадлежащих владельцу. Связь `OneToMany` с сущностью `Pet` означает, что один владелец может иметь несколько питомцев.

**Зачем это нужно:**

Модель `Owner` позволяет хранить и управлять информацией о клиентах ветеринарной клиники, включая их контактные данные и список питомцев.

### 2. Pet (Питомец)

Класс `Pet` представляет животное, принадлежащее владельцу.

**Код класса:**


```java
@Entity
@Table(name = "pets")
public class Pet extends NamedEntity {

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private PetType type;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pet")
    private Set<Visit> visits = new HashSet<>();

    // Геттеры и сеттеры
}
```


**Описание полей:**

- `birthDate`: Дата рождения питомца.
- `type`: Тип питомца (например, собака, кошка). Связь `ManyToOne` с сущностью `PetType`.
- `owner`: Владелец питомца. Связь `ManyToOne` с сущностью `Owner`.
- `visits`: Список визитов питомца к ветеринару. Связь `OneToMany` с сущностью `Visit`.

**Зачем это нужно:**

Модель `Pet` позволяет хранить информацию о животных, включая их тип, дату рождения, владельца и историю посещений клиники.

### 3. PetType (Тип питомца)

Класс `PetType` определяет вид питомца, например, собака или кошка.

**Код класса:**


```java
@Entity
@Table(name = "types")
public class PetType extends NamedEntity {
    // Дополнительные поля и методы могут быть добавлены при необходимости
}
```


**Зачем это нужно:**

Модель `PetType` используется для классификации питомцев по видам, что упрощает управление и фильтрацию данных.

### 4. Visit (Визит)

Класс `Visit` представляет посещение питомцем ветеринарной клиники.

**Код класса:**


```java
@Entity
@Table(name = "visits")
public class Visit extends BaseEntity {

    @Column(name = "visit_date")
    private LocalDate date;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    // Геттеры и сеттеры
}
```


**Описание полей:**

- `date`: Дата визита.
- `description`: Описание или причина визита.
- `pet`: Питомец, который посетил клинику. Связь `ManyToOne` с сущностью `Pet`.

**Зачем это нужно:**

Модель `Visit` позволяет отслеживать историю медицинских посещений каждого питомца, что важно для ведения медицинских записей и планирования последующего ухода.

### 5. Vet (Ветеринар)

Класс `Vet` представляет ветеринарного врача, работающего в клинике.

**Код класса:**


```java
@Entity
@Table(name = "vets")
public class Vet extends Person {

    @ManyToMany
    @JoinTable(name = "vet_specialties", joinColumns = @JoinColumn(name = "vet_id"),
            inverseJoinColumns = @JoinColumn(name = "specialty_id"))
    private Set<Specialty> specialties = new HashSet<>();

    // Геттеры и сеттеры
}
```


**Описание полей:**

- `specialties`: Набор специализаций ветеринара. Связь `ManyToMany` с сущностью `Specialty`.

**Зачем это нужно:**

Модель `Vet` хранит информацию о врачах и их специализациях, что помогает в назначении подходящего специалиста для каждого случая.

### 6. Specialty (Специализация)

Класс `Specialty` определяет область специализации ветеринара, например, кардиология или дерматология.

**Код класса:**


```java
@Entity
@Table(name = 