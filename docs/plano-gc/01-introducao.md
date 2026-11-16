# 1. Introdução

> Rascunho da Pessoa 1. Estrutura baseada no Plano de Gerenciamento de Configuração do modelo RUP / DATASUS.

## 1.1 Propósito

Este documento define como a configuração do **Spring PetClinic (fork `petclinic-cicd`)** é gerenciada ao longo do trabalho: o que é controlado, quem controla, como as mudanças entram no repositório e como as versões são identificadas e entregues. Seu objetivo é garantir rastreabilidade, integridade e reprodutibilidade do build, dos testes e da implantação, que são automatizados por um pipeline no Jenkins.

Público-alvo: os cinco integrantes do grupo, o professor da disciplina e quem for avaliar o projeto.

## 1.2 Escopo

**Dentro do escopo**
- Código-fonte da aplicação (Java 17, Spring Boot 4.1.0, Maven, H2 em memória).
- Scripts de build e do pipeline (`pom.xml`, `Jenkinsfile`, `Dockerfile`).
- Testes automatizados de unidade, integração e aceitação.
- Documentação do projeto e este Plano.
- Artefatos gerados: `.jar` e imagem Docker versionada.

**Fora do escopo**
- Evolução funcional do PetClinic: a aplicação é usada como objeto de estudo e não ganha funcionalidades novas.
- Bancos MySQL e PostgreSQL do upstream (o pipeline usa apenas H2).
- Configuração do ambiente de produção além do deploy gratuito/container exigido pelo trabalho.

**Ciclo de vida coberto:** do commit na `main` até a entrega ao usuário (build → testes de unidade e integração → teste de aceitação → lançamento).

## 1.3 Definições, acrônimos e abreviaturas

| Termo | Definição |
|---|---|
| GC | Gerência (ou Gerenciamento) de Configuração |
| IC | Item de Configuração: artefato sob controle de versão e de mudança |
| Baseline | Versão aprovada de um conjunto de ICs, usada como base para as próximas |
| CI/CD | Integração Contínua / Entrega (ou Implantação) Contínua |
| PR | Pull Request: pedido de integração de uma branch à `main` |
| SemVer | Versionamento Semântico (`MAJOR.MINOR.PATCH`) |
| Tag | Marca imutável no Git que identifica uma versão (`vX.Y.Z`) |
| Pipeline | Sequência automatizada de estágios executada pelo Jenkins |
| IT | Teste de integração (`*IT.java`), executado pelo maven-failsafe-plugin |
| Upstream | Repositório original `spring-projects/spring-petclinic` |
| Fork | Cópia do upstream na conta do grupo (`petclinic-cicd`) |
| RUP | Rational Unified Process |
| DATASUS | Departamento de Informática do SUS, cujo modelo de plano de GC é a referência |

## 1.4 Visão geral do documento

| Seção | Conteúdo | Responsável |
|---|---|---|
| 1 | Introdução | P1 |
| 2 | Papéis e responsabilidades; ferramentas e ambiente | P2 |
| 3 | Identificação dos itens de configuração | P1 |
| 4 | Baselines | P1 |
| 5 | Controle de mudanças | P3 |
| 6 | Relatório de status e auditorias | P4 |
| 7 | Gestão de releases, marcos e cronograma | P5 |

## 1.5 Referências

1. Spring PetClinic — https://github.com/spring-projects/spring-petclinic
2. Plano de Gerenciamento de Configuração — modelo RUP/DATASUS.
3. IEEE Std 828 — Configuration Management in Systems and Software Engineering.
4. Versionamento Semântico 2.0.0 — https://semver.org/lang/pt-BR/
5. Conventional Commits 1.0.0 — https://www.conventionalcommits.org/pt-br/
6. Documentação do Jenkins — https://www.jenkins.io/doc/
7. `CONTRIBUTING.md` e `CLAUDE.md` deste repositório (fluxo de branches, commits e convenção de testes).
8. `docs/inventario-testes.md` (testes existentes e do grupo).
