# Changelog

Todas as mudanças relevantes deste projeto são registradas aqui.
Formato baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/) e versionamento [SemVer](https://semver.org/lang/pt-BR/).

## [Não lançado]

### Adicionado
- `CLAUDE.md`, `CONTRIBUTING.md` e template de Pull Request.
- Testes de integração (`*IT`) para donos, pets, visitas e veterinários, executados pelo maven-failsafe-plugin.
- Rascunhos do Plano de Gerência de Configuração em `docs/plano-gc/`.

### Alterado
- Versão do projeto: `4.0.0-SNAPSHOT` (upstream) → `1.0.0-SNAPSHOT`.
- `pom.xml`: configuração do maven-failsafe-plugin; `PostgresIntegrationTests` e `MySqlIntegrationTests` excluídos do surefire (exigem Docker e outros bancos).

### Removido
- Build Gradle (`build.gradle`, `settings.gradle`, `gradlew*`, `gradle/`): o projeto usa apenas Maven.
- `.github/dco.yml` herdado do upstream (bot DCO não faz parte do fluxo do grupo).
- Workflows do GitHub Actions (o CI do grupo é o Jenkins).

## Base

Fork de [spring-projects/spring-petclinic](https://github.com/spring-projects/spring-petclinic) no commit `500158f`.
