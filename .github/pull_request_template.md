## Descrição

<!-- O que muda e por quê. -->

## Tipo de mudança

- [ ] feature
- [ ] fix
- [ ] docs
- [ ] ci / build / chore

## Área alterada (marque o dono se não for você)

- [ ] `Jenkinsfile` — P2
- [ ] `Dockerfile` / deploy / release — P5
- [ ] Testes de unidade (`*Test.java`) — P3
- [ ] Testes de aceitação — P4
- [ ] Testes de integração (`*IT.java`), `pom.xml`, documentos do repositório — P1
- [ ] Código da aplicação (`src/main`)

## Checklist

- [ ] A branch segue o padrão `feature|fix|docs/pN-descricao`
- [ ] Commits no padrão Conventional Commits, em português
- [ ] `./mvnw verify` passa localmente (build ok)
- [ ] Testes ok: unidade (`./mvnw test`) e integração (`./mvnw verify`)
- [ ] Novos testes usam o sufixo `*Test` (unidade) ou `*IT` (integração)
- [ ] Nenhum teste existente foi apagado
- [ ] `CHANGELOG.md` atualizado, se a mudança for visível ao usuário
- [ ] Avisei o dono da área, caso tenha alterado arquivo de outra pessoa
