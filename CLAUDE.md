# CLAUDE.md — petclinic-cicd

## 1. Contexto

Trabalho Prático 1 (10 pts) de **Gerência de Configuração e Evolução de Software** (Engenharia de Software, PUC Minas), em grupo de 5 pessoas. Tema do grupo: **Plataforma de CI/CD – Jenkins**.

Projeto escolhido: **Spring PetClinic** (Java + Spring Boot + Maven, H2 em memória). Este repositório é um fork: `VitorCostaVianna/petclinic-cicd` (upstream: `spring-projects/spring-petclinic`).

### O que o grupo entrega
1. **Plano de Gerenciamento de Configuração** (PDF), no modelo de GC do DATASUS / RUP.
2. **Pipeline no Jenkins**, disparado a cada commit na `main`:
   - **Commit:** build + testes automatizados de **unidade** e de **integração**
   - **Teste de Aceitação:** ao menos 1 teste automatizado (funcional e/ou não funcional)
   - **Lançamento:** entrega aos usuários (deploy em servidor gratuito ou container Docker)
3. **Apresentação** de até 10 min (plano de GC, tecnologias, pipeline, lições aprendidas).

Entregáveis: PDF do Plano de GC, `.zip` do repositório, `Jenkinsfile`, PDF dos slides.

### Divisão do grupo
| Pessoa | Responsabilidade |
|---|---|
| **P1** | Repositório (fork, main protegida, branches, versionamento/tags), build `./mvnw package`, **testes de integração**, Plano: Introdução, Itens de Configuração, Baselines; PDF final do Plano e `.zip` |
| **P2** | Jenkins em Docker, webhook/Poll SCM, `Jenkinsfile` (todos os estágios + Build). Plano: Papéis e Responsabilidades, Ferramentas e Ambiente |
| **P3** | Testes de **unidade** (JUnit), JaCoCo publicado no Jenkins, pipeline falha se teste quebrar. Plano: Controle de Mudanças |
| **P4** | Teste de **aceitação** (Selenium) + estágio Jenkins que sobe o container, roda o teste e derruba. Plano: Relatório de Status e Auditorias. Slides |
| **P5** | `Dockerfile`, estágio de Release (imagem com tag da versão, Docker Hub, Render/Railway ou container), rollback. Plano: Gestão de Releases, Marcos e Cronograma |

Ordem: P1 e P2 primeiro (todos dependem do repo e do Jenkins); depois P3, P4 e P5 em paralelo, cada um na sua branch, com PR para `main`.

## 2. Stack e comandos

- **Java 17** (mínimo exigido pelo `maven-enforcer-plugin` / `java.version` no `pom.xml`); funciona com JDKs mais novos.
- **Spring Boot 4.1.0**, Maven (usar sempre o wrapper `./mvnw`; no Windows PowerShell, `.\mvnw.cmd`).
- Banco padrão: **H2 em memória** (perfis `mysql` e `postgres` existem, mas não são usados no pipeline).
- Porta da aplicação: **8080** → http://localhost:8080 (actuator em `/actuator`).

| Objetivo | Comando |
|---|---|
| Compilar | `./mvnw compile` |
| Build completo (jar) | `./mvnw package` |
| Rodar a aplicação | `./mvnw spring-boot:run` ou `java -jar target/*.jar` |
| Só testes de unidade | `./mvnw test` |
| Unidade + integração | `./mvnw verify` |
| Só integração (`*IT`) | `./mvnw verify -Dtest=NoSuchTest -Dsurefire.failIfNoSpecifiedTests=false` (pula os de unidade; será validado após configurar o failsafe) |

> O build já roda `spring-javaformat:validate` e checkstyle (nohttp) na fase `validate`. Se falhar por formatação, rode `./mvnw spring-javaformat:apply`.

## 3. Convenção de testes

| Tipo | Padrão de nome | Plugin | Fase | Comando |
|---|---|---|---|---|
| Unidade | `*Test.java` (o PetClinic original usa `*Tests.java`, que o surefire também executa) | maven-surefire-plugin | `test` | `./mvnw test` |
| Integração | `*IT.java` | maven-failsafe-plugin | `integration-test` / `verify` | `./mvnw verify` |

Assim o Jenkins roda cada tipo num estágio separado (`./mvnw test` e `./mvnw verify`). Testes novos do grupo **devem** seguir esses sufixos, para ficar claro o que foi escrito por nós.

## 4. Regras de Git

- **Nunca** commitar direto na `main`; tudo entra por **Pull Request**.
- Branches: `feature/pN-descricao`, `fix/pN-descricao`, `docs/pN-descricao` (N = número da pessoa).
- Commits em **Conventional Commits, em português**: `feat:`, `fix:`, `test:`, `docs:`, `ci:`, `build:`, `chore:`.
  Ex.: `test: adiciona IT de cadastro de dono`.
- **SemVer** com tags `vX.Y.Z` na `main`.
- Todo PR precisa passar no build e nos testes.

## 5. Dono de cada arquivo

| Arquivo / área | Dono |
|---|---|
| `Jenkinsfile` | P2 |
| `Dockerfile`, deploy, release | P5 |
| `src/test/**/*Test.java` (unidade) | P3 |
| Testes de aceitação (Selenium) | P4 |
| `src/test/**/*IT.java` | P1 |
| `pom.xml` (plugins de teste/build) | P1 |
| Documentos do repositório (`README`, `CONTRIBUTING`, `CHANGELOG`, `.github/`, `docs/plano-gc/01,03,04`) | P1 |

Para mexer na área de outra pessoa, **avisar no PR** (marcar o dono).

## 6. Regras para o Claude

- Código, nomes de classes/métodos/variáveis em **inglês** (seguindo o PetClinic); documentação e mensagens de commit em **português**.
- **Não apagar** testes existentes.
- **Não** fazer push na `main`, **não** alterar proteção de branch e **não** fazer force push sem perguntar antes.
- Não criar tags nem remover workflows sem aprovação explícita.
- Trabalhar em passos pequenos, um commit por passo.
