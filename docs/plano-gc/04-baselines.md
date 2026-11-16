# 4. Baselines

> Rascunho da Pessoa 1.

## 4.1 Conceito

Baseline é um conjunto de ICs em versões específicas, revisado e aprovado, que serve de base para o trabalho seguinte e só muda por meio do controle de mudanças. No projeto, cada baseline é identificada por uma **tag Git** na `main`, imutável.

## 4.2 Baselines do projeto

O fluxo segue a progressão Desenvolvimento → Teste → Produção, alinhada aos estágios do pipeline.

| Baseline | Nome | O que congela | Critério de entrada | Identificação |
|---|---|---|---|---|
| BL-0 | Base do upstream | Código original do PetClinic no momento do fork | Fork criado | commit `500158f`; tag `v0.0.0-upstream` *(opcional)* |
| BL-1 | **Desenvolvimento** | Estado da `main` após cada merge de PR | PR aprovado (1 revisão) e estágio *Commit* verde (build + testes de unidade + integração) | commit na `main`; sem tag (rastreado pelo build do Jenkins) |
| BL-2 | **Teste** (candidata a release) | Conjunto de ICs que passou no teste de aceitação | Estágio *Teste de Aceitação* verde sobre a imagem/container gerado | tag `vX.Y.Z-rc.N` |
| BL-3 | **Produção** | Release entregue ao usuário | BL-2 aprovada pelo gerente de configuração e estágio *Lançamento* concluído | tag `vX.Y.Z` + imagem Docker `X.Y.Z` |

Marco adicional: **BL-D (documentação)** é a versão final do Plano de GC e dos slides, marcada junto à release final (`vX.Y.Z`), pois os PDFs entregues referenciam essa versão.

## 4.3 Quando as baselines são criadas

| Baseline | Evento | Quem cria | Como |
|---|---|---|---|
| BL-1 | Merge de PR na `main` | Quem aprova o PR (merge) | automático; o Jenkins dispara o pipeline |
| BL-2 | Pipeline da `main` passou até o aceite e o grupo decidiu congelar a versão | P1 | `git tag -a vX.Y.Z-rc.N -m "..." && git push origin vX.Y.Z-rc.N` |
| BL-3 | Release candidata validada e entregue | P1 (tag) e P5 (imagem e deploy) | `git tag -a vX.Y.Z -m "..."`; o estágio *Lançamento* publica a imagem com a tag da versão |

Regras:
- Tags só na `main`, nunca em branches de trabalho; **tag publicada não é movida nem apagada**. Se houver erro, cria-se uma nova versão.
- Toda tag `vX.Y.Z` atualiza o `CHANGELOG.md` (seção da versão e data) no mesmo PR que antecede a tag.
- A versão do `pom.xml` acompanha a tag (`X.Y.Z`); entre releases fica `X.Y.Z-SNAPSHOT`.

## 4.4 Esquema de versões

SemVer `MAJOR.MINOR.PATCH`:
- **MAJOR**: mudança incompatível (ex.: troca de versão do Java exigida ou do banco).
- **MINOR**: funcionalidade ou capacidade nova compatível (ex.: novo estágio de pipeline que muda a entrega).
- **PATCH**: correção sem mudança de comportamento.

Proposta de linha do tempo:

| Versão | Conteúdo |
|---|---|
| `v1.0.0-rc.1` | Pipeline completo funcionando até o aceite |
| `v1.0.0` | Primeira release entregue (imagem no Docker Hub e deploy) |
| `v1.0.1` ... | Correções posteriores (ex.: rollback testado) |

## 4.5 Aprovação

| Baseline | Aprova | Evidência |
|---|---|---|
| BL-1 | Revisor do PR (outra pessoa do grupo) | aprovação no PR + build verde no Jenkins |
| BL-2 | Gerente de configuração (P1) com ciência do grupo | relatório do estágio de aceitação + tag `-rc` |
| BL-3 | Grupo, em consenso, registrado em ata/PR da release | tag `vX.Y.Z`, imagem Docker, `CHANGELOG.md` |

## 4.6 Rollback

Voltar a uma baseline anterior significa reimplantar a imagem Docker da tag anterior (responsabilidade da P5, ver seção 7); o código não é revertido com `reset`, e sim com um PR de reversão (`git revert`) para preservar o histórico.

## 4.7 Auditoria de baselines

Cada baseline pode ser reconstruída a partir de: tag Git + `pom.xml`/`Jenkinsfile`/`Dockerfile` da tag + número do build do Jenkins. A seção 6 (P4) descreve como isso é verificado.
