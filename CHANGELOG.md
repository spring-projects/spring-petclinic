# Changelog

Todas as mudanças relevantes deste projeto são registradas aqui.
Formato baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/) e versionamento [SemVer](https://semver.org/lang/pt-BR/).

## [Não lançado]

### Adicionado
- `CLAUDE.md`, `CONTRIBUTING.md` e template de Pull Request.
- Testes de integração (`*IT`) para donos, pets, visitas e veterinários, executados pelo maven-failsafe-plugin.
- Rascunhos do Plano de Gerência de Configuração em `docs/plano-gc/`.
- 34 testes de unidade (`*Test`) para `BaseEntity`, `NamedEntity`/`Person`, `Owner`, `Pet`, `Visit`, `Vet`, `Vets` e `WebConfiguration`.
- `Jenkinsfile`: o estágio *Cobertura* falha o build se o JaCoCo ficar abaixo de 85% de instruções, 75% de branches ou 85% de linhas.
- Teste de aceitação com Selenium (`OwnerRegistrationAT`, profile Maven `acceptance`) e `ci/acceptance.sh`, que sobe a aplicação e o Chrome em containers, roda o teste e remove os containers. Usa um Dockerfile provisório (`ci/acceptance.Dockerfile`) até o Dockerfile da P5.
- `Jenkinsfile`: o estágio *Teste de aceitação* publica o relatório JUnit e os screenshots de falha.
- Seção 6 (Relatório de Status e Auditorias) do Plano de GC.

### Alterado
- Versão do projeto: `4.0.0-SNAPSHOT` (upstream) → `1.0.0-SNAPSHOT`.
- `pom.xml`: configuração do maven-failsafe-plugin; `PostgresIntegrationTests` e `MySqlIntegrationTests` excluídos do surefire (exigem Docker e outros bancos).

### Removido
- Build Gradle (`build.gradle`, `settings.gradle`, `gradlew*`, `gradle/`): o projeto usa apenas Maven.
- `.github/dco.yml` herdado do upstream (bot DCO não faz parte do fluxo do grupo).
- Workflows do GitHub Actions (o CI do grupo é o Jenkins).

## Base

Fork de [spring-projects/spring-petclinic](https://github.com/spring-projects/spring-petclinic) no commit `500158f`.
