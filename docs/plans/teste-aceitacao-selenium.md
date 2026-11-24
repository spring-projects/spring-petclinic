# Teste de aceitação com Selenium — Plano de implementação

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Entregar `ci/acceptance.sh` + teste Selenium `OwnerRegistrationAT` para o estágio "Teste de aceitação" do Jenkins ficar verde.

**Architecture:** O script builda uma imagem provisória da aplicação (`ci/acceptance.Dockerfile`), sobe a aplicação e um `selenium/standalone-chrome` numa rede Docker própria do build e roda o profile Maven `acceptance` (failsafe, `*AT.java`) apontando para o Selenium. Ao final, um `trap` remove tudo. O Jenkins publica o relatório JUnit e os screenshots de falha.

**Tech Stack:** Java 17, JUnit 5, Selenium 4.43.0 (versão do BOM do Spring Boot 4.1.0), `selenium/standalone-chrome:4.43.0-20260404`, `eclipse-temurin:17-jre`, Bash, Jenkins (Docker-in-Docker).

**Spec:** `docs/specs/2026-11-24-teste-aceitacao-selenium.md`

## Global Constraints
- Branch `feature/p4-teste-aceitacao`; commits Conventional Commits em português, um por tarefa.
- Código em inglês; docs e mensagens do script em português.
- `selenium-java` **sem versão** no `pom.xml` (gerenciada pelo Boot); imagem do Selenium fixada em `4.43.0-20260404`.
- Testes de aceitação usam sufixo `*AT.java` e **não** rodam em `./mvnw test` nem `./mvnw verify` (CA07).
- Relatórios em `target/acceptance-reports/`; screenshots em `target/acceptance-screenshots/` (D6, D10).
- Espera da aplicação e do Selenium: até 120 s; espera por elemento: 10 s (sem `sleep` fixo no teste).
- Asserções por dados digitados, classe `has-error` e URL; nunca por texto traduzido (D9).
- Arquivos de outros donos: `pom.xml` e `.gitattributes` (P1), `Jenkinsfile` (P2) — listar no PR.
- Rodar `./mvnw spring-javaformat:apply` antes de cada commit com Java.

## Review Focus
- **CRLF no `ci/acceptance.sh`** (checkout no Windows) → bash no Jenkins quebra com `$'\r'`. Teste: `.gitattributes` com `*.sh text eol=lf` e `git ls-files --eol ci/acceptance.sh` mostrando `i/lf` (Tarefa 2).
- **Bit de execução ausente** → `test -x` do Jenkinsfile falha. Teste: `git ls-files -s ci/acceptance.sh` com modo `100755` (Tarefa 2).
- **Jar ausente ou mais de um jar** em `target/` → mensagem clara. Teste: rodar o script sem `target/*.jar` → exit 1 e "jar não encontrado" (Tarefa 2).
- **Chrome cai por falta de memória compartilhada** → `--shm-size=2g` no container do Selenium (Tarefa 2).
- **Job interrompido pelo timeout do Jenkins** → `trap` em `EXIT INT TERM`; T04 cobre a limpeza (Tarefa 2).

---

### Task 1: Profile `acceptance` e teste `OwnerRegistrationAT` (CA01, CA02, CA07, RN01, RN02)

**Files:**
- Modify: `pom.xml` (dependência em `<dependencies>`; profile novo em `<profiles>`)
- Create: `src/test/java/org/springframework/samples/petclinic/acceptance/OwnerRegistrationAT.java`

**Interfaces:**
- Produces: execução failsafe `acceptance-tests` (só no profile `acceptance`), invocada como `./mvnw -Pacceptance test-compile failsafe:integration-test@acceptance-tests failsafe:verify@acceptance-tests`.
- Produces: propriedades de sistema lidas pelo teste: `acceptance.seleniumUrl` (padrão `http://localhost:4444`) e `acceptance.baseUrl` (padrão `http://petclinic:8080`, a URL que o **navegador** acessa).

- [ ] **Step 1: Dependência e profile no `pom.xml`**
  - `org.seleniumhq.selenium:selenium-java`, `<scope>test</scope>`, sem `<version>`.
  - Profile `acceptance` com `maven-failsafe-plugin`, execução `id=acceptance-tests`, goals `integration-test` e `verify`, configuração: `includes` = `**/*AT.java`; `reportsDirectory` e `summaryFile` = `${project.build.directory}/acceptance-reports` (+ `/failsafe-summary.xml`).
  - O `<includes>**/*IT.java</includes>` global continua como está. É ele que garante CA07.

- [ ] **Step 2: Escrever `OwnerRegistrationAT`**
  - `@BeforeEach`: `new RemoteWebDriver(URI.create(seleniumUrl).toURL(), new ChromeOptions())` e `WebDriverWait` de 10 s.
  - Screenshot: extensão `@RegisterExtension` que implementa `AfterTestExecutionCallback` (roda **antes** do `@AfterEach`). Se `context.getExecutionException().isPresent()`, grava `target/acceptance-screenshots/<nomeDoMetodo>.png`. `@AfterEach` faz `driver.quit()`.
  - Helper privado `registerOwner(String firstName, String lastName, String telephone)`: abre `{baseUrl}/owners/new`, preenche `firstName`, `lastName`, `address`=`Rua Teste 1`, `city`=`Belo Horizonte`, `telephone` e envia `#add-owner-form`.
  - Helper privado `searchOwners(String lastName)`: abre `{baseUrl}/owners/find`, preenche `lastName` e envia `#search-owner-form`.
  - `shouldListBothOwners_whenTwoOwnersShareTheSearchedLastName` (T01):
    - `registerOwner("Ana", "Acceptance", "3199990001")` → URL casa com `/owners/\d+$`;
    - `registerOwner("Bruno", "Acceptance", "3199990002")`;
    - `searchOwners("Acceptance")` → `#owners tbody tr` tem exatamente 2 linhas;
    - os textos das linhas contêm `Ana Acceptance` + `3199990001` e `Bruno Acceptance` + `3199990002`.
  - `shouldRejectOwner_whenTelephoneHasFiveDigits` (T02):
    - `registerOwner("Carla", "Rejected", "12345")` → URL termina em `/owners/new`; o `.form-group` que contém `#telephone` tem a classe `has-error`;
    - `searchOwners("Rejected")` → continua em `/owners` sem `#owners`; o formulário `#search-owner-form` mostra o bloco de erro (`#lastNameGroup` contém um `p` de erro).

- [ ] **Step 3: Rodar e ver falhar (red: sem infraestrutura)**
  Run: `./mvnw -Pacceptance test-compile failsafe:integration-test@acceptance-tests failsafe:verify@acceptance-tests`
  Expected: BUILD FAILURE com erro de conexão ao Selenium em `localhost:4444` (ainda não há Selenium nem aplicação). Isso prova que o teste roda pelo profile; o green vem na Tarefa 2.

- [ ] **Step 4: Verificar CA07 (T06)**
  Run: `./mvnw verify`
  Expected: BUILD SUCCESS; `target/acceptance-reports/` não existe; nenhum `*AT` em `target/surefire-reports` ou `target/failsafe-reports`.

- [ ] **Step 5: Commit**
  `test: adiciona teste de aceitação Selenium de cadastro de dono`

---

### Task 2: Dockerfile provisório e `ci/acceptance.sh` (CA01–CA05, CA08)

**Files:**
- Create: `ci/acceptance.Dockerfile`
- Create: `ci/acceptance.sh` (modo `100755`)
- Modify: `.gitattributes` (`*.sh text eol=lf`)

**Interfaces:**
- Consumes: comando Maven e propriedades `acceptance.seleniumUrl` / `acceptance.baseUrl` da Tarefa 1.
- Produces: `ci/acceptance.sh` sem argumentos; sai com 0 se o aceite passou e ≠ 0 caso contrário. Variáveis opcionais: `BUILD_TAG` (Jenkins), `DOCKER_HOST`, `SELENIUM_HOST` (sobrescreve o host).

- [ ] **Step 1: `ci/acceptance.Dockerfile`**
  `FROM eclipse-temurin:17-jre`, `COPY spring-petclinic-*.jar /app/app.jar`, `EXPOSE 8080`, `ENTRYPOINT ["java","-jar","/app/app.jar"]`. Contexto do build = `target/`. Comentário no topo: provisório, substituir pelo `Dockerfile` da P5.

- [ ] **Step 2: `ci/acceptance.sh`**
  - `#!/usr/bin/env bash`, `set -euo pipefail`, `export MSYS_NO_PATHCONV=1`, `cd` para a raiz do repositório.
  - Verifica que existe exatamente um `target/spring-petclinic-*.jar`. Senão: `echo "ERRO: jar não encontrado em target/ (rode ./mvnw package)"` e `exit 1`.
  - `id` = `BUILD_TAG` (ou `local-$$`) em minúsculas, com caracteres fora de `[a-z0-9.-]` trocados por `-`. Nomes: rede `petclinic-at-$id`, app `petclinic-at-app-$id`, Selenium `petclinic-at-selenium-$id`, imagem `petclinic-at:$id`.
  - Função `cleanup` com `trap cleanup EXIT INT TERM`: guarda `$?`; se ≠ 0 e o container da app existir, imprime `docker logs --tail 200` da app; depois `docker rm -f` nos dois containers, `docker network rm` e `docker rmi`, com `|| true` em cada um; sai com o código guardado.
  - `docker build -f ci/acceptance.Dockerfile -t <imagem> target`.
  - `docker network create`; app com `--network-alias petclinic`; Selenium com `--shm-size=2g -p 4444` (porta aleatória) e imagem `selenium/standalone-chrome:4.43.0-20260404`.
  - Host: `SELENIUM_HOST` se definido; senão o host de `DOCKER_HOST` (`tcp://docker:2376` → `docker`); senão `localhost`. Porta: `docker port <selenium> 4444/tcp`, primeira linha, depois do último `:`.
  - Espera da app (até 120 s, a cada 2 s): `docker exec <selenium> curl -fs http://petclinic:8080/actuator/health` contendo `"UP"`. Se esgotar: `echo "ERRO: aplicação não subiu em 120 s"` e `exit 1`.
  - Espera do Selenium (até 120 s): `curl -fs http://$host:$port/status` contendo `"ready":true`. Se esgotar: `echo "ERRO: Selenium não subiu em 120 s"` e `exit 1`.
  - Roda o comando Maven da Tarefa 1 com `-B --no-transfer-progress -Dacceptance.seleniumUrl=http://$host:$port -Dacceptance.baseUrl=http://petclinic:8080`.

- [ ] **Step 3: `.gitattributes` e bit de execução**
  Acrescentar `*.sh text eol=lf`; `git add ci/ .gitattributes`; `git update-index --chmod=+x ci/acceptance.sh`.
  Verify: `git ls-files -s ci/acceptance.sh` → `100755`; `git ls-files --eol ci/acceptance.sh` → `i/lf`.

- [ ] **Step 4: Jar ausente**
  Run (com `target/` sem jar, ex.: depois de `./mvnw clean`): `bash ci/acceptance.sh; echo "exit=$?"`
  Expected: `ERRO: jar não encontrado...` e `exit=1`.

- [ ] **Step 5: Green local (T01, T02, CA08)**
  Run: `./mvnw package -DskipTests && bash ci/acceptance.sh; echo "exit=$?"`
  Expected: `Tests run: 2, Failures: 0`, `exit=0`, `target/acceptance-reports/TEST-*OwnerRegistrationAT.xml` existe.

- [ ] **Step 6: Limpeza (T04)**
  Run: `docker ps -a --filter name=petclinic-at; docker network ls --filter name=petclinic-at`
  Expected: nenhuma linha além do cabeçalho.

- [ ] **Step 7: Falha propositalmente (T03 local + T04 em falha), sem commitar**
  Trocar `3199990002` por `0000000000` na asserção do T01 → rodar o script → `exit≠0`, log da app impresso, `target/acceptance-screenshots/shouldListBothOwners_whenTwoOwnersShareTheSearchedLastName.png` existe, Step 6 continua vazio. Reverter com `git checkout -- src/test`.

- [ ] **Step 8: App que não sobe (T05), sem commitar**
  Trocar temporariamente o `ENTRYPOINT` por `["sleep","300"]` e rodar o script → depois de ~120 s, `ERRO: aplicação não subiu em 120 s` e `exit≠0`; Step 6 vazio. Reverter com `git checkout -- ci/acceptance.Dockerfile`.

- [ ] **Step 9: Commit**
  `ci: adiciona script de teste de aceitação com Dockerfile provisório`

---

### Task 3: Publicação no Jenkins e registro (CA03, CA06)

**Files:**
- Modify: `Jenkinsfile` (estágio `Teste de aceitação`)
- Modify: `docs/inventario-testes.md` (linha da P4 + contagem), `CHANGELOG.md` (Adicionado), `docs/plano-gc/03-itens-de-configuracao.md:30` (IC-07 sem *(previsto)*, local `src/test/java/.../acceptance/*AT.java` + `ci/acceptance.sh`)

**Interfaces:**
- Consumes: `target/acceptance-reports/TEST-*.xml` e `target/acceptance-screenshots/*.png` da Tarefa 2.

- [ ] **Step 1: `post` no estágio**
  ```groovy
  post {
      always {
          junit testResults: 'target/acceptance-reports/TEST-*.xml', allowEmptyResults: true
          archiveArtifacts artifacts: 'target/acceptance-screenshots/*.png', allowEmptyArchive: true
      }
  }
  ```
  `allowEmptyResults: true` porque, se a aplicação não subir, não existe relatório e o erro real é o do script.

- [ ] **Step 2: Docs**
  Inventário: P4 | Aceitação | `*AT.java` | `src/test/java/.../petclinic/acceptance/`, com uma linha "Em 24/11/2026: 2 testes `*AT`". CHANGELOG: teste de aceitação Selenium + `ci/acceptance.sh` + Dockerfile provisório.

- [ ] **Step 3: Verificação local**
  Run: `./mvnw verify`
  Expected: BUILD SUCCESS (formatação e testes intactos).

- [ ] **Step 4: Commit**
  `ci: publica relatório e screenshots do teste de aceitação no Jenkins`

- [ ] **Step 5: Verificação no Jenkins (CA03, CA06), depois do push autorizado pelo humano**
  Build da branch `feature/p4-teste-aceitacao`: o estágio "Teste de aceitação" fica verde e a aba Tests mostra `OwnerRegistrationAT` com 2 testes. Prints em `docs/plano-gc/imagens/jenkins-p4-*.png`. Para o CA03, um commit temporário com a asserção quebrada deixa o estágio vermelho, com o screenshot nos artefatos e o Lançamento ignorado; depois esse commit é revertido.
