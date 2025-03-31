### 🔍 Разбираем архитектуру Spring PetClinic

**Spring PetClinic** — это демонстрационное приложение, которое показывает лучшие практики разработки на **Spring Boot**. Оно представляет собой веб-приложение для ветеринарной клиники, где можно управлять владельцами, питомцами и ветеринарами.

Проект использует **Spring Boot, Spring MVC, Spring Data JPA, Hibernate и Thymeleaf** (шаблонизатор), а в качестве базы данных — **H2 или MySQL/PostgreSQL**.

---

## 📂 1. Общая структура проекта

Когда ты скачиваешь репозиторий [Spring PetClinic](https://github.com/spring-projects/spring-petclinic.git) и открываешь его в IDE (например, IntelliJ IDEA), файловая структура выглядит так:

```plaintext
spring-petclinic/
│── src/
│   ├── main/
│   │   ├── java/org/springframework/samples/petclinic/
│   │   │   ├── model/          # Сущности (Entity)
│   │   │   ├── repository/      # Репозитории (DAO)
│   │   │   ├── service/         # Логика (Сервисы)
│   │   │   ├── web/             # Контроллеры (REST API + MVC)
│   │   │   ├── PetClinicApplication.java  # Точка входа в приложение
│   ├── resources/
│   │   ├── application.properties  # Конфигурация Spring Boot
│   │   ├── db/                     # SQL-скрипты для инициализации БД
│   │   ├── templates/               # Шаблоны Thymeleaf для UI
│── pom.xml  # Файл зависимостей Maven
```

---

## ⚙ 2. Основные компоненты проекта

Spring PetClinic построен по классической **трёхслойной архитектуре**:

1. **Model (Модель)** — хранит сущности и их взаимосвязи.
2. **Repository (Репозиторий/DAO)** — отвечает за работу с базой данных.
3. **Service (Сервис)** — содержит бизнес-логику.
4. **Web (Контроллер)** — обработка HTTP-запросов и взаимодействие с UI.

Разберём каждый слой подробно.

---

## 📌 3. Модель (Model)

Этот слой отвечает за **представление данных** и использует **аннотации JPA** для работы с базой.

Пример: **сущность `Owner` (владелец питомца)**
Файл: `model/Owner.java`

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

### 🔹 Разбор кода:

- `@Entity` — указывает, что это сущность для базы данных.
- `@Table(name = "owners")` — связывает с таблицей `owners` в БД.
- `@Column(name = "city")` — отображает поле в столбец `city`.
- `@OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")` — указывает, что один владелец может иметь **много питомцев** (связь **"один ко многим"**).

Пример другой сущности: **Pet (питомец)**

```java
@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    // Геттеры и сеттеры
}
```

### 🔹 Здесь:

- `@ManyToOne` — питомец связан **с одним владельцем**.
- `@JoinColumn(name = "owner_id")` — связывает `Pet` с `Owner` по `owner_id`.

---

## 📌 4. Репозиторий (Repository)

Этот слой отвечает за взаимодействие с базой данных.

Пример: **репозиторий для владельцев**
Файл: `repository/OwnerRepository.java`

```java
public interface OwnerRepository extends JpaRepository<Owner, Integer> {

    @Query("SELECT o FROM Owner o WHERE o.lastName LIKE :lastName%")
    Collection<Owner> findByLastName(@Param("lastName") String lastName);
}
```

### 🔹 Разбор кода:

- `extends JpaRepository<Owner, Integer>` — позволяет использовать готовые методы (`findById()`, `save()`, `deleteById()`).
- `@Query("SELECT o FROM Owner o WHERE o.lastName LIKE :lastName%")` — кастомный запрос для поиска владельцев **по фамилии**.

PetClinic использует **Spring Data JPA**, поэтому многие методы создаются **автоматически**.

---

## 📌 5. Сервис (Service)

Этот слой реализует **бизнес-логику**, вызывая методы репозитория.

Пример: **сервис для владельцев**
Файл: `service/OwnerService.java`

```java
@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

    @Autowired
    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Owner findOwnerById(int id) {
        return ownerRepository.findById(id).orElse(null);
    }

    public void saveOwner(Owner owner) {
        ownerRepository.save(owner);
    }
}
```

### 🔹 Разбор кода:

- `@Service` — аннотация, указывающая, что этот класс — **сервис**.
- `findOwnerById(int id)` — возвращает владельца по ID.
- `saveOwner(Owner owner)` — сохраняет владельца в БД.

---

## 📌 6. Контроллер (Web)

Контроллеры принимают **HTTP-запросы** и передают их в сервис.

Пример: **контроллер для владельцев**
Файл: `web/OwnerController.java`

```java
@RestController
@RequestMapping("/owners")
public class OwnerController {

    private final OwnerService ownerService;

    @Autowired
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping("/{ownerId}")
    public ResponseEntity<Owner> getOwner(@PathVariable int ownerId) {
        Owner owner = ownerService.findOwnerById(ownerId);
        return owner != null ? ResponseEntity.ok(owner) : ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<Owner> createOwner(@RequestBody Owner owner) {
        ownerService.saveOwner(owner);
        return ResponseEntity.status(HttpStatus.CREATED).body(owner);
    }
}
```

### 🔹 Разбор кода:

- `@RestController` — этот класс **обрабатывает HTTP-запросы**.
- `@GetMapping("/{ownerId}")` — **возвращает владельца** по ID.
- `@PostMapping("/")` — **создаёт нового владельца**.

---

## 🚀 7. Точка входа в приложение

Файл: `PetClinicApplication.java`

```java
@SpringBootApplication
public class PetClinicApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetClinicApplication.class, args);
    }
}
```

Здесь **Spring Boot автоматически запускает приложение**.

---

## 🔥 Итог

Архитектура Spring PetClinic:

1. **Модель (Model)** — сущности (Owner, Pet, Vet).
2. **Репозиторий (Repository)** — доступ к данным через `Spring Data JPA`.
3. **Сервис (Service)** — бизнес-логика.
4. **Контроллер (Web)** — обработка REST-запросов.
5. **Точка входа (Main Class)** — запуск приложения.
