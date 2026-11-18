# 3. Identificação dos Itens de Configuração

> Rascunho da Pessoa 1. Itens marcados como *(previsto)* ainda serão criados pelos responsáveis.

## 3.1 Critério de seleção

Um artefato é Item de Configuração (IC) quando (a) é necessário para construir, testar, entregar ou entender a aplicação, (b) pode mudar ao longo do projeto e (c) a mudança precisa ser rastreada. Artefatos gerados (`target/`, relatórios) **não** são ICs de código: são reproduzíveis a partir dos ICs.

## 3.2 Nomenclatura

- **Branches:** `feature/pN-descricao`, `fix/pN-descricao`, `docs/pN-descricao` (N = pessoa).
- **Commits:** Conventional Commits em português (`feat:`, `fix:`, `test:`, `docs:`, `ci:`, `build:`, `chore:`).
- **Tags/versões:** `vX.Y.Z` (SemVer); pré-lançamentos como `vX.Y.Z-rc.N`.
- **Imagem Docker:** `<usuario-dockerhub>/petclinic-cicd:X.Y.Z` (e `latest` apontando para a última release estável).
- **Testes:** unidade `*Test.java`; integração `*IT.java`; upstream `*Tests.java`.
- **Documentos:** Markdown em `docs/`, nomes em minúsculas com hífen; PDFs finais gerados a partir deles.

## 3.3 Lista de Itens de Configuração

Armazenamento de todos os ICs: repositório Git `github.com/VitorCostaVianna/petclinic-cicd`, exceto onde indicado.

| ID | Item | Tipo | Local | Identificação / nomenclatura | Responsável |
|---|---|---|---|---|---|
| IC-01 | Código-fonte da aplicação | Código | `src/main/java/**` | pacotes `org.springframework.samples.petclinic.*`; versão pela tag | Grupo (upstream) |
| IC-02 | Recursos da aplicação (templates, SQL H2, mensagens, CSS) | Código | `src/main/resources/**`, `src/main/scss/**` | arquivos do upstream | Grupo (upstream) |
| IC-03 | Script de build Maven | Build | `pom.xml` | `groupId:artifactId:version` | P1 |
| IC-04 | Maven Wrapper | Build | `mvnw`, `mvnw.cmd`, `.mvn/**` | versão do Maven fixada no wrapper | P1 |
| IC-05 | Testes de unidade do grupo | Teste | `src/test/**/*Test.java` (exceção: `I18nPropertiesSyncTest` é do upstream) | `*Test.java` | P3 |
| IC-06 | Testes de integração do grupo | Teste | `src/test/java/**/petclinic/it/*IT.java` | `*IT.java` | P1 |
| IC-07 | Testes de aceitação (Selenium) *(previsto)* | Teste | definido pela P4 | definido pela P4 | P4 |
| IC-08 | Testes do upstream | Teste | `src/test/**/*Tests.java` e `I18nPropertiesSyncTest.java` | `*Tests.java`; não alterar | Upstream |
| IC-09 | Pipeline Jenkins *(previsto)* | Pipeline | `Jenkinsfile` | um arquivo na raiz | P2 |
| IC-10 | Configuração do Jenkins *(previsto)* | Infra | repositório/pasta definida pela P2 (ex.: `docker-compose` do Jenkins) | definido pela P2 | P2 |
| IC-11 | Dockerfile da aplicação *(previsto)* | Entrega | `Dockerfile` | um arquivo na raiz | P5 |
| IC-12 | Imagem Docker | Artefato de release | Docker Hub | `<usuario>/petclinic-cicd:X.Y.Z` | P5 |
| IC-13 | Pacote executável (`.jar`) | Artefato de build | gerado em `target/`; guardado como artefato do build no Jenkins | `spring-petclinic-<versão>.jar` | P2 |
| IC-14 | Documentos do repositório | Documentação | `README.md`, `CONTRIBUTING.md`, `CLAUDE.md`, `.github/pull_request_template.md` | nomes fixos | P1 |
| IC-15 | Plano de Gerenciamento de Configuração | Documentação | `docs/plano-gc/*.md` e PDF final | `NN-secao.md`; PDF `plano-gc-vX.Y.Z.pdf` | P1 (montagem); seções por pessoa |
| IC-16 | Inventário de testes | Documentação | `docs/inventario-testes.md` | nome fixo | P1 |
| IC-17 | Slides da apresentação | Documentação | definido pela P4 (PDF) | `slides-vX.Y.Z.pdf` | P4 |
| IC-18 | Configuração de qualidade (checkstyle, formatação) | Configuração | `src/checkstyle/**`, `.editorconfig` | arquivos do upstream | P1 |
| IC-19 | Histórico de mudanças | Documentação | `CHANGELOG.md` | seções por versão | P1 |
| IC-20 | Configuração de proteção da `main` | Configuração | configurações do GitHub (comando em `CONTRIBUTING.md`) | regra da branch `main` | P1 |

## 3.4 Itens que não são ICs

- `target/`, relatórios de teste e de cobertura (reproduzíveis; publicados no Jenkins por build).
- Arquivos de IDE (`.idea/`, `.vscode/`) e variáveis locais.
- Resíduos do upstream fora do escopo do trabalho: `k8s/`, `.devcontainer/`, `.gitpod.yml`, `docker-compose.yml` (bancos MySQL/Postgres). O build Gradle foi removido; o projeto usa apenas Maven.
- Credenciais (Docker Hub, tokens): ficam no cofre de credenciais do Jenkins e **nunca** no repositório.

## 3.5 Origem dos itens

O código-fonte e os testes do upstream foram herdados do fork no commit `500158f`. Tudo que o grupo cria segue as convenções acima, o que permite distinguir a contribuição do grupo (ver `docs/inventario-testes.md`).

## 3.6 Controle de acesso

- Todos os integrantes devem ser colaboradores com acesso de escrita ao repositório (convites enviados pelo administrador, P1). A `main` é protegida por regra de branch: **ninguém** escreve direto nela, e toda mudança entra por PR com 1 aprovação (ver `CONTRIBUTING.md`).
- Enquanto o Jenkins não reporta status ao GitHub, a exigência de build e testes verdes no PR é verificada pelo revisor (a regra de status obrigatório será ligada quando a P2 integrar o Jenkins).
- Mudanças em IC de outro responsável exigem avisar o dono no PR.
