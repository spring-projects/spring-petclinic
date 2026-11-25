# 6. Relatório de Status e Auditorias

> Seção da Pessoa 4. Os dados do exemplo (6.6) foram levantados em 24/11/2026.

## 6.1 Objetivo

Esta seção define como o grupo registra e informa o estado dos Itens de Configuração (relatório de status, ou *contabilização da configuração*) e como verifica se as baselines estão íntegras (auditorias). O relatório responde "em que estado está cada IC e o que mudou". A auditoria responde "a baseline faz o que deveria e corresponde ao que está versionado".

## 6.2 Fontes de informação

O grupo não mantém registro paralelo. Todo dado do relatório e da auditoria sai de uma fonte que já existe e que pode ser consultada de novo:

| Fonte | O que fornece | Como consultar |
|---|---|---|
| Git (`main` e tags) | commits, autores, datas, versões | `git log --oneline`, `git tag -l`, `git show <tag>` |
| Pull Requests no GitHub | o quê, quem pediu, quem aprovou, quando integrou (seção 5.8) | aba *Pull requests* ou `gh pr list --state merged` |
| Jenkins | resultado de cada estágio por commit, relatórios JUnit, cobertura JaCoCo, aceite | job `petclinic-cicd` → branch → build |
| `CHANGELOG.md` | mudanças por versão | arquivo na raiz |
| `docs/inventario-testes.md` | quantidade de testes por tipo e por pessoa | arquivo em `docs/` |
| Docker Hub | imagens publicadas e suas tags (P5, seção 7) | página do repositório da imagem |

## 6.3 Relatórios de status

| Relatório | Conteúdo | Quando | Quem gera | Onde fica |
|---|---|---|---|---|
| Status do pipeline | resultado de cada estágio (Build, Unidade, Integração, Cobertura, Aceitação, Lançamento) | a cada build | Jenkins (automático) | página do build no Jenkins |
| Testes e cobertura | testes executados e falhas por tipo; % de instruções, branches e linhas | a cada build | Jenkins (automático) | abas *Tests* e *Coverage* do build |
| Mudanças do período | PRs integrados, autor, revisor, data | antes de cada baseline BL-2/BL-3 | P4 | este documento (modelo em 6.6) |
| Status dos ICs | situação de cada IC da seção 3 | antes de cada baseline BL-2/BL-3 | P4 | este documento (modelo em 6.6) |
| Não conformidades | achados das auditorias e como foram tratados | após cada auditoria | auditor | este documento (6.5) |

Os dois primeiros saem sozinhos do pipeline e são a fonte do dia a dia. Os outros três são consolidados pela P4 nos marcos de versão, porque é neles que o grupo precisa decidir se a versão pode avançar.

## 6.4 Auditorias

### 6.4.1 Quando e quem

| Auditoria | Momento | Auditor |
|---|---|---|
| Funcional (FCA) | antes de criar cada tag `vX.Y.Z-rc.N` (BL-2) e `vX.Y.Z` (BL-3) | um integrante que não é autor das mudanças da versão |
| Física (PCA) | antes de criar cada tag `vX.Y.Z` (BL-3) e na entrega final do trabalho | P4, com apoio da P1 (repositório) e da P5 (imagem) |

A tag só é criada depois que a auditoria correspondente fica sem não conformidade aberta (seção 4.3).

### 6.4.2 Auditoria funcional: a baseline faz o que deveria?

Verifica, pelo resultado do pipeline no commit que vai receber a tag, que os requisitos foram testados:

- [ ] O build do Jenkins desse commit está verde em Build, Testes de unidade, Testes de integração e Cobertura.
- [ ] A cobertura está acima dos mínimos (85% de instruções, 75% de branches, 85% de linhas).
- [ ] O estágio Teste de aceitação está verde, e os testes `*AT` aparecem na aba *Tests* sem falha.
- [ ] Nenhum teste foi desativado (`@Disabled`) ou removido em relação à baseline anterior (`git diff <tag-anterior> -- src/test`).
- [ ] Toda mudança da versão entrou por PR aprovado (seção 5).

### 6.4.3 Auditoria física: o que foi entregue corresponde ao versionado?

Verifica que a baseline pode ser reconstruída (seção 4.7) e que os artefatos entregues saem dela:

- [ ] A tag existe na `main` e aponta para o commit auditado (`git rev-list -n 1 vX.Y.Z`).
- [ ] O `pom.xml` da tag tem a versão `X.Y.Z`, sem `-SNAPSHOT` (seção 4.3).
- [ ] O `CHANGELOG.md` da tag tem a seção da versão, com data.
- [ ] O `Jenkinsfile` e o `Dockerfile` da tag são os que geraram o build auditado (número do build anotado).
- [ ] A imagem `X.Y.Z` existe no Docker Hub e foi publicada pelo build da tag (log do estágio Lançamento).
- [ ] Todo IC da seção 3 está no local e com a nomenclatura indicada.
- [ ] Não há credenciais no repositório (seção 3.4).

## 6.5 Não conformidades

Um item do checklist não atendido é uma não conformidade (NC). Tratamento:

1. O auditor registra a NC na tabela abaixo, com a evidência.
2. O responsável pelo IC abre um PR de correção (`fix:` ou `docs:`), seguindo a seção 5.
3. A NC é fechada quando o PR é integrado e o item do checklist passa.
4. Enquanto houver NC aberta que afete a baseline, a tag não é criada.

| ID | Data | Auditoria | Achado | Evidência | Responsável | Situação |
|---|---|---|---|---|---|---|
| NC-01 | 24/11/2026 | Física | IC-09 (`Jenkinsfile`) e IC-10 (configuração do Jenkins) ainda aparecem como *(previsto)* na seção 3, mas já estão na `main` desde o PR #2 | `docs/plano-gc/03-itens-de-configuracao.md`, linhas de IC-09 e IC-10 | P1 | aberta |
| NC-02 | 24/11/2026 | Física | IC-13 diz que o `.jar` é guardado como artefato do build no Jenkins, mas o `Jenkinsfile` não arquiva o jar | `Jenkinsfile` sem `archiveArtifacts` do jar | P2 | aberta |
| NC-03 | 24/11/2026 | Funcional | O PR #1 (setup do repositório) foi integrado sem aprovação registrada; os PRs #2 a #4 têm uma aprovação cada | `gh api .../pulls/1/reviews` retorna lista vazia | P1 | aberta: confirmar com a P1 se a proteção da `main` já estava ativa nesse merge |
| NC-04 | 24/11/2026 | Funcional | O estágio Teste de aceitação falha em todo build da `main` porque `ci/acceptance.sh` não existe | seções 2.6 e 5.10 | P4 | em correção (PR `feature/p4-teste-aceitacao`) |

## 6.6 Exemplo real: relatório de status de 24/11/2026

### Mudanças integradas na `main`

| PR | Branch | Autor | Aprovação | Integrado em |
|---|---|---|---|---|
| #1 | `feature/p1-setup-repositorio` | P1 | — (NC-03) | 16/11/2026 |
| #2 | `feature/p2-jenkins` | P2 | 1 (P1) | 18/11/2026 |
| #3 | `feature/p3-testes-unidade` | P3 | 1 (P2) | 18/11/2026 |
| #4 | `docs/p3-controle-mudancas` | P3 | 1 (P2) | 18/11/2026 |

### Testes e cobertura

| Tipo | Quantidade | Responsável | Fonte |
|---|---|---|---|
| Upstream (`*Tests`) | 77 | — | inventário de testes |
| Unidade do grupo (`*Test`) | 34 | P3 | inventário de testes |
| Integração (`*IT`) | 12 | P1 | inventário de testes |
| Aceitação (`*AT`) | 2 (em PR) | P4 | `OwnerRegistrationAT` |

Cobertura do último build verde até o estágio Cobertura: 90% de instruções, 87% de branches e 95% de linhas (Figura 9, seção 5.10).

### Status dos ICs

| Situação | ICs |
|---|---|
| Entregue na `main` | IC-01 a IC-06, IC-08, IC-09, IC-10, IC-14 a IC-16, IC-18 a IC-20 |
| Em andamento | IC-07 (testes de aceitação, P4), IC-17 (slides, P4) |
| Pendente | IC-11 (Dockerfile, P5), IC-12 (imagem Docker, P5); IC-13 parcial (NC-02) |

### Baselines

| Baseline | Situação |
|---|---|
| BL-0 | commit `500158f` identificado; tag opcional `v0.0.0-upstream` não criada |
| BL-1 | ativa: cada merge na `main` (último: PR #4) |
| BL-2 | não criada: depende do estágio de aceitação verde (NC-04) |
| BL-3 | não criada: depende do Dockerfile, da imagem e do deploy (P5) |

A única tag do repositório é `1.5.x`, herdada do upstream. Ela não segue o padrão `vX.Y.Z` e não é baseline do grupo.

## 6.7 Evidências

As capturas do estágio Teste de aceitação no Jenkins (verde, e vermelho com um teste quebrado de propósito) serão adicionadas aqui como Figuras 12 e 13 depois da validação do PR `feature/p4-teste-aceitacao`.
