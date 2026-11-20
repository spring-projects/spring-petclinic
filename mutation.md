# Lab 4 – Mutation Testing con PITest

## Configurazione

È stato integrato **PITest 1.30.2** con `pitest-junit5-plugin 1.2.3`.

Sono stati configurati:
- Target: `org.springframework.samples.petclinic.*`
- Mutation Threshold: 80%
- Coverage Threshold: 80%
- Report: HTML e XML

## Analisi dei risultati

L'analisi iniziale di `PetValidator` ha prodotto un Mutation Score del 92% (11/12).

Una mutazione sopravvissuta modificava la condizione `name.length() > 30` in `>= 30`. Aggiungendo un test sul caso limite di 30 caratteri, lo score è salito al **100%**.

Successivamente, nel package `owner`, è stato individuato un problema in `Visit.getDescription()`. Un nuovo test JUnit ha aumentato lo score dal 79% all'80%, superando il Quality Gate.

### Risultati finali del progetto

| Metrica | Risultato |
|---|---|
| Line Coverage PITest | 97% |
| Mutation Score | **87%** |
| Mutazioni generate | 175 |
| Mutazioni eliminate | 152 |
| Mutazioni sopravvissute | 16 |
| Mutazioni senza copertura | 7 |
| Test Strength | 90% |

**Esito finale: BUILD SUCCESS**

## Miglioramenti futuri

È possibile migliorare ulteriormente il Mutation Score aggiungendo test per le 16 mutazioni sopravvissute e per i percorsi non coperti.

## Conclusioni

PITest ha dimostrato che un'elevata copertura del codice non garantisce necessariamente l'efficacia dei test.

I nuovi test JUnit hanno migliorato la capacità di rilevare errori, raggiungendo un **Mutation Score finale dell'87%**, superiore alla soglia richiesta.

**Comando:** `./mvnw test-compile pitest:mutationCoverage`