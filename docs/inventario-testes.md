# Inventário dos testes

Levantamento feito em 16/11/2026 sobre o fork, antes das contribuições do grupo (commit `500158f`, do upstream).

## Testes que já vinham no PetClinic (upstream)

| Classe | Tipo | Técnica | Nº de testes | O que testa |
|---|---|---|---|---|
| `model/ValidatorTests` | Unidade | Bean Validation | 2 | Nome e sobrenome vazios não validam (`Person`) |
| `owner/OwnerTests` | Unidade | JUnit | 2 | `Owner.addPet`: adiciona pet persistido e não duplica |
| `owner/PetTypeFormatterTests` | Unidade | Mockito | 3 | Conversão texto ↔ `PetType` |
| `owner/PetValidatorTests` | Unidade | Mockito | classes aninhadas | `PetValidator`: nome, tipo e data de nascimento inválidos |
| `vet/VetTests` | Unidade | JUnit | 1 | Serialização de `Vet` |
| `system/CrashControllerTests` | Unidade | JUnit | 1 | `CrashController` lança exceção de propósito |
| `system/I18nPropertiesSyncTest` | Unidade | JUnit | 2 | Arquivos `messages_*.properties` sincronizados e sem textos não internacionalizados |
| `owner/OwnerControllerTests` | Fatia web (unidade) | `@WebMvcTest` + mocks | 18 | `OwnerController` |
| `owner/PetControllerTests` | Fatia web (unidade) | `@WebMvcTest` + mocks | classes aninhadas | `PetController` |
| `owner/VisitControllerTests` | Fatia web (unidade) | `@WebMvcTest` + mocks | 5 | `VisitController` |
| `vet/VetControllerTests` | Fatia web (unidade) | `@WebMvcTest` + mocks | 3 | `VetController` |
| `system/WelcomeControllerTests` | Fatia web (unidade) | `@WebMvcTest` | 1 | Página inicial |
| `service/ClinicServiceTests` | Fatia de persistência | `@DataJpaTest` + H2 | 12 | Repositórios e JPA |
| `PetClinicIntegrationTests` | Integração | `@SpringBootTest` (porta aleatória) | 3 | Aplicação completa: cache de vets, `/owners/1` e lista de donos |
| `PetClinicConcurrencyTests` | Integração | `@SpringBootTest` | 1 | Corrida ao criar pet com nome duplicado é bloqueada |
| `system/CrashControllerIntegrationTests` | Integração | `@SpringBootTest` | 2 | Página de erro |
| `MySqlIntegrationTests` | Integração (**excluído**) | Testcontainers + MySQL | 2 | Aplicação com MySQL |
| `PostgresIntegrationTests` | Integração (**excluído**) | Docker Compose + PostgreSQL | 2 | Aplicação com PostgreSQL |

Observações:
- Os testes do upstream usam o sufixo `*Tests` e rodam todos no surefire (`./mvnw test`), inclusive os que são de integração.
- `MySqlIntegrationTests` e `PostgresIntegrationTests` foram excluídos do surefire no `pom.xml`: precisam de Docker, o do Postgres falhou no `docker compose up` e o do MySQL leva cerca de 3 minutos. O pipeline usa H2.
- Total do upstream que roda no pipeline: **77 testes**.

## Testes escritos pelo grupo

| Pessoa | Tipo | Sufixo | Local |
|---|---|---|---|
| P1 | Integração | `*IT.java` | `src/test/java/.../petclinic/it/` |
| P3 | Unidade | `*Test.java` | `src/test/java/...` |
| P4 | Aceitação | a definir pela P4 | a definir pela P4 |

Em 16/11/2026: **12 testes `*IT`** da P1 (`OwnerFlowIT` 6, `PetFlowIT` 2, `VisitFlowIT` 2, `VetFlowIT` 2).

## Como mostrar na apresentação o que é do grupo

1. **Sufixos distintos**: tudo que o grupo escreve termina em `*Test` (unidade) ou `*IT` (integração), enquanto o upstream usa `*Tests`. Um `find . -name "*IT.java"` já mostra a autoria.
2. **Pacote próprio**: os `*IT` ficam em `.../petclinic/it/`.
3. **Relatório separado**: o Jenkins publica surefire (unidade) e failsafe (integração) em estágios distintos, e o JaCoCo mostra a cobertura antes e depois.
4. **Comparativo no slide**: "upstream: 77 testes → grupo: +12 IT +N unidade +M aceitação", obtido do `git log --diff-filter=A --name-only` sobre `src/test`.
5. **Histórico**: commits `test:` por pessoa (`git shortlog -sn --grep='^test'`).
