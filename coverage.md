# Lab 4 – Code Coverage con JaCoCo

## Configurazione

È stato utilizzato **JaCoCo 0.8.15**, configurato nel `pom.xml` con tre esecuzioni:

- `prepare-agent`: raccolta dei dati di copertura.
- `report`: generazione del report HTML/XML.
- `check`: verifica delle soglie nella fase `verify`.

**Quality Gates** applicati a livello di package:
- Line Coverage ≥ 80%
- Branch Coverage ≥ 75%
- Class Coverage = 100%

## Analisi dei risultati

La prima esecuzione ha evidenziato una copertura insufficiente nel package principale, con l'8% di Line Coverage e il 50% di Class Coverage.

Sono stati aggiunti due test JUnit per `PetClinicRuntimeHints`, ottenendo:

- **Line Coverage: 83,3%**
- **Class Coverage: 100%**
- **Instruction Coverage: 92%**

La classe `PetClinicRuntimeHints` ha raggiunto il 100% delle istruzioni coperte, mentre `PetClinicApplication` presenta ancora una copertura parziale (37%).

## Miglioramenti futuri

Si prevede di aggiungere test per i metodi e i percorsi non coperti, con particolare attenzione alla classe `PetClinicApplication` e ai casi limite.

## Conclusioni

JaCoCo ha permesso di individuare le aree poco testate e migliorare la copertura attraverso nuovi test JUnit.

**Comando di verifica:** `./mvnw clean verify`