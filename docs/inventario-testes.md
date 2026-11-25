# Inventário dos testes

Levantamento feito em 16/11/2026 sobre o fork, antes das contribuições do grupo (commit `500158f`, do upstream).

## Testes que já vinham no PetClinic (upstream)

| Classe | Tipo | Técnica | Nº de testes | O que testa |
|---|---|---|---|---|
| `model/ValidatorTests` | Unidade | Bean Validation | 2 | Nome e sobrenome vazios não validam (`Person`) |
| `owner/OwnerTests` | Unidade | JUnit | 2 | `Owner.addPet`: adiciona pet persistido e não duplica |
| `owner/PetTypeFormatterTests` | Unidade | Mockito | 3 | Conversão texto ↔ `PetType` |
| `owner/PetValidatorTests` | Unidade | Mockito | 7 (3 + 4 aninhados) | `PetValidator`: nome, tipo e data de nascimento inválidos |
| `vet/VetTests` | Unidade | JUnit | 1 | Serialização de `Vet` |
| `system/CrashControllerTests` | Unidade | JUnit | 1 | `CrashController` lança exceção de propósito |
| `system/I18nPropertiesSyncTest` | Unidade | JUnit | 2 | Arquivos `messages_*.properties` sincronizados e sem textos não internacionalizados |
| `owner/OwnerControllerTests` | Fatia web (unidade) | `@WebMvcTest` + mocks | 18 | `OwnerController` |
| `owner/PetControllerTests` | Fatia web (unidade) | `@WebMvcTest` + mocks | 14 (com aninhados) | `PetController` |
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
- Exceção à regra de sufixos: `system/I18nPropertiesSyncTest` é do upstream, mas termina em `*Test`. Para separar autoria, use também o pacote e o histórico do Git.
- Os testes do upstream usam o sufixo `*Tests` e rodam todos no surefire (`./mvnw test`), inclusive os que são de integração.
- `MySqlIntegrationTests` e `PostgresIntegrationTests` foram excluídos do surefire no `pom.xml`: precisam de Docker. No levantamento (16/11/2026, Windows com Docker Desktop), o do Postgres falhou no `docker compose up` (exit code 1) e o do MySQL passou em cerca de 3 minutos (Testcontainers; sem Docker ele é pulado). O pipeline usa H2.
- Total do upstream que roda no pipeline: **77 testes**.

## Testes escritos pelo grupo

| Pessoa | Tipo | Sufixo | Local |
|---|---|---|---|
| P1 | Integração | `*IT.java` | `src/test/java/.../petclinic/it/` |
| P3 | Unidade | `*Test.java` | `src/test/java/...` |
| P4 | Aceitação | `*AT.java` | `src/test/java/.../petclinic/acceptance/` |

Em 16/11/2026: **12 testes `*IT`** da P1 (`OwnerFlowIT` 6, `PetFlowIT` 2, `VisitFlowIT` 2, `VetFlowIT` 2).

Em 17/11/2026: **34 testes de unidade `*Test`** da P3. Eles são JUnit puro, sem subir o Spring, e por isso rodam em milissegundos:

| Classe | Nº de testes | O que testa |
|---|---|---|
| `model/BaseEntityTest` | 3 | `isNew()` conforme o `id` |
| `model/NamedEntityTest` | 3 | `toString()` de `NamedEntity` (com e sem nome) e dados de `Person` |
| `owner/OwnerTest` | 13 | `getPet` por nome (sem diferenciar maiúsculas), por id e ignorando pets novos; `addPet` (nulo e id repetido); `addVisit` e suas validações; `toString()` |
| `owner/PetTest` | 4 | Data de nascimento, tipo e lista de visitas (ordem e sem duplicar) |
| `owner/VisitTest` | 2 | Data padrão (dia seguinte) e descrição |
| `vet/VetTest` | 4 | Especialidades ordenadas por nome, contagem, sem duplicar e cópia defensiva |
| `vet/VetsTest` | 2 | Lista de veterinários inicializada sob demanda |
| `system/WebConfigurationTest` | 3 | Idioma padrão inglês e troca pelo parâmetro `lang` |

Resultado (`./mvnw clean verify`, Java 17): **111 testes de unidade** (77 do upstream + 34 da P3) e **12 de integração**, todos passando.

Cobertura JaCoCo (unidade + integração, mesmo arquivo `target/jacoco.exec` usado pelo Jenkins):

| Métrica | Antes (`main`, só upstream + IT) | Depois (com os testes da P3) |
|---|---|---|
| Instruções | 89,7% | 90,4% |
| Branches | 82,8% | 87,7% |
| Linhas | 94,0% | 94,9% |

O estágio *Cobertura* do `Jenkinsfile` exige no mínimo 85% de instruções, 75% de branches e 85% de linhas. Abaixo disso, o build falha.

Em 24/11/2026: **2 testes de aceitação `*AT`** da P4 em `acceptance/OwnerRegistrationAT`, com Selenium e Chrome em container: cadastro de dois donos com o mesmo sobrenome aparecendo na lista da busca, e cadastro recusado com telefone inválido. Rodam só pelo `ci/acceptance.sh` (profile Maven `acceptance`), fora de `./mvnw test` e `./mvnw verify`.

## Como mostrar na apresentação o que é do grupo

1. **Sufixos distintos**: tudo que o grupo escreve termina em `*Test` (unidade) ou `*IT` (integração), enquanto o upstream usa `*Tests` (exceção: `I18nPropertiesSyncTest`). Um `find . -name "*IT.java"` já mostra a autoria.
2. **Pacote próprio**: os `*IT` ficam em `.../petclinic/it/`.
3. **Relatório separado**: o Jenkins publica surefire (unidade) e failsafe (integração) em estágios distintos, e o JaCoCo mostra a cobertura antes e depois.
4. **Comparativo no slide**: "upstream: 77 testes → grupo: +12 IT +34 unidade +2 aceitação", obtido do `git log --diff-filter=A --name-only` sobre `src/test`.
5. **Histórico**: commits `test:` por pessoa (`git shortlog -sn --grep='^test'`).
