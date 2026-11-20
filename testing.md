# Report Unit Testing - Spring PetClinic

## 1. Architettura della Test Suite (JUnit 6 e Mockito 5)
La suite di test del progetto è stata estesa con test unitari puri in isolamento (`*UnitTest.java`), configurando nel `pom.xml` **JUnit Jupiter 6.1.3** e **Mockito 5.24.0**. I test sono organizzati rispecchiando i package applicativi (`model`, `owner`, `vet`, `system`):
- **Modelli di Dominio e Validazione:** Test unitari deterministici su `BaseEntity`, `Owner`, `Pet`, `Vet` e `PetValidator` per verificare la logica interna (gestione entità nuove o già persistite, ordinamento alfabetico delle specializzazioni, associazione delle visite e regole di validazione dei form).
- **Controller in Isolamento:** Utilizzo di `@ExtendWith(MockitoExtension.class)`, `@Mock` e `@InjectMocks` su `OwnerController`, `VetController` e `CrashController` per simulare i repository (`OwnerRepository`, `VetRepository`), verificando viste restituite, attributi del modello e interazioni (`verify`) senza avviare il contesto Spring né il database.

## 2. Applicazione dei Principi FIRST
Ogni caso di test è progettato secondo i cinque principi cardine del Test-Driven Development:
- **Fast:** L'assenza di container Spring, server web e connessioni al database consente l'esecuzione dei test unitari in pochi millisecondi.
- **Independent:** Ogni metodo `@Test` è completamente autosufficiente e sfrutta `@BeforeEach` per inizializzare uno stato pulito prima di ciascuna esecuzione, eliminando qualsiasi dipendenza dall'ordine di lancio.
- **Repeatable:** I test impiegano dati fissi e deterministici (es. date esplicite tramite `LocalDate.of(...)` anziché valori correnti o casuali), garantendo risultati identici su qualsiasi ambiente di esecuzione.
- **Self-validating:** L'esito è verificato automaticamente tramite asserzioni esplicite (`assertEquals`, `assertTrue`, `assertFalse`, `assertNull`, `assertThrows`) corredate da messaggi diagnostici, senza necessità di ispezioni manuali.
- **Thorough:** La progettazione non mira alla sola copertura percentuale delle righe, ma esercita sistematicamente flussi nominali, casi limite e percorsi di errore.

## 3. Selezione dei Test Case ed Edge Case Coperti
Per massimizzare l'efficacia nell'individuazione dei difetti, i test combinano tecniche **Black-Box** (*Equivalence Partitioning*, *Boundary Value Analysis*) e **White-Box** (*Branch Coverage*):
- **Input Invalidi e Valori Limite:** Verifica di campi `null`, stringhe vuote o composte da soli spazi (`"   "`) in `PetValidatorUnitTest`, oltre alla copertura dei rami condizionali per pet nuovi (`id == null`, tipo obbligatorio) rispetto a pet già esistenti (`id != null`).
- **Gestione Eccezioni e Percorsi Critici:** Verifica del lancio controllato di `IllegalArgumentException` in `OwnerUnitTest` e `OwnerControllerUnitTest` in caso di ID inesistenti, parametri nulli o associazioni non valide, e verifica di `RuntimeException` in `CrashControllerUnitTest`.
- **Integrazione CI/CD e Sicurezza:** L'intera suite (107 test) viene eseguita automaticamente nella pipeline GitHub Actions (`mvn test`), archiviando i report Surefire (`target/surefire-reports/`) come artefatti e mantenendo il codice privo di credenziali o dati sensibili hardcodati.