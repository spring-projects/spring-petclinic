# Guia de Contribuição

Este repositório é o fork `petclinic-cicd` do Spring PetClinic, usado no Trabalho Prático 1 de Gerência de Configuração e Evolução de Software (PUC Minas). Todo o grupo segue o fluxo abaixo.

## Fluxo de trabalho

1. Atualize a `main` local: `git checkout main && git pull`.
2. Crie uma branch a partir da `main` com o nome no formato `tipo/pN-descricao`:
   - `feature/pN-descricao` — nova funcionalidade, teste ou pipeline
   - `fix/pN-descricao` — correção
   - `docs/pN-descricao` — documentação

   `N` é o número da pessoa (P1 a P5). Exemplo: `feature/p3-testes-unidade`.
3. Trabalhe em passos pequenos, com um commit por passo.
4. Rode localmente antes de abrir o PR: `./mvnw verify`.
5. Envie a branch (`git push -u origin <branch>`) e abra um **Pull Request** para a `main`, preenchendo o template.
6. O PR só é integrado com **1 aprovação** de outra pessoa do grupo e com build e testes passando.
7. Use "Squash and merge" ou "Rebase and merge" para manter o histórico linear.

**Nunca** faça commit ou push direto na `main`.

## Mensagens de commit

Padrão [Conventional Commits](https://www.conventionalcommits.org/pt-br/), **em português**:

| Prefixo | Uso |
|---|---|
| `feat:` | nova funcionalidade |
| `fix:` | correção de bug |
| `test:` | criação ou alteração de testes |
| `docs:` | documentação |
| `ci:` | pipeline, Jenkinsfile, workflows |
| `build:` | `pom.xml`, Dockerfile, dependências |
| `chore:` | tarefas de manutenção |

Exemplo: `test: adiciona IT de cadastro de dono`.

## Convenção de testes

| Tipo | Nome | Plugin | Comando |
|---|---|---|---|
| Unidade | `*Test.java` | maven-surefire-plugin (fase `test`) | `./mvnw test` |
| Integração | `*IT.java` | maven-failsafe-plugin (fase `integration-test`/`verify`) | `./mvnw verify` |

Testes novos do grupo devem usar esses sufixos. Os testes que já vinham no PetClinic usam `*Tests.java` e não devem ser apagados.
`PostgresIntegrationTests` e `MySqlIntegrationTests` ficam fora do pipeline (exigem Docker e outros bancos).

## Dono de cada área

| Arquivo / área | Dono |
|---|---|
| `Jenkinsfile` | P2 |
| `Dockerfile`, deploy e release | P5 |
| Testes de unidade (`*Test.java`) | P3 |
| Testes de aceitação | P4 |
| Testes de integração (`*IT.java`), `pom.xml` (plugins de teste/build), documentos do repositório | P1 |

Se o seu PR altera a área de outra pessoa, marque-a no PR e avise o grupo.

## Versionamento

[SemVer](https://semver.org/lang/pt-BR/) com tags `vX.Y.Z` criadas **somente na `main`**, depois do merge (ver `docs/plano-gc/04-baselines.md`). Cada tag marca uma baseline e atualiza o `CHANGELOG.md`.

## Proteção da `main`

A `main` deve exigir Pull Request com 1 aprovação. Quem é administrador do repositório (P1) roda **uma vez**, após revisar o comando:

```bash
gh api -X PUT repos/VitorCostaVianna/petclinic-cicd/branches/main/protection \
  --input - <<'JSON'
{
  "required_status_checks": null,
  "enforce_admins": false,
  "required_pull_request_reviews": {
    "required_approving_review_count": 1,
    "dismiss_stale_reviews": true
  },
  "restrictions": null,
  "required_linear_history": true,
  "allow_force_pushes": false,
  "allow_deletions": false
}
JSON
```

Observações:
- `required_status_checks` está `null` porque o CI é o Jenkins (externo). Quando o Jenkins reportar status ao GitHub, trocar por `{"strict": true, "contexts": ["<nome-do-check>"]}`.
- `enforce_admins: false` permite que o administrador contorne a regra em emergências; use `true` para valer também para ele.
- Para conferir: `gh api repos/VitorCostaVianna/petclinic-cicd/branches/main/protection`.
