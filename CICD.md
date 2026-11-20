# Report CI/CD - Spring PetClinic

## 1. Architettura della Pipeline (`ci.yml`)
La pipeline è configurata per attivarsi automaticamente in seguito agli eventi di `push` e `pull_request` sul branch `main`. Le fasi principali (steps) sono:
* **Checkout code:** Utilizza `actions/checkout@v4` per clonare il repository all'interno del runner.
* **Set up JDK 17:** Utilizza `actions/setup-java@v4` per installare Java (distribuzione Temurin). Sfrutta anche il parametro `cache: 'maven'` per velocizzare le build successive salvando le dipendenze scaricate.
* **Run Maven Build and Tests:** Esegue i test automatizzati tramite il wrapper Maven (`./mvnw clean test`).
* **Package & Upload:** Se i test passano, compila il progetto (`./mvnw package -DskipTests`) e salva l'artefatto `.jar` generato usando `actions/upload-artifact@v4`.

## 2. Garanzia di Riproducibilità della Build (Build Reproducibility)
La pipeline garantisce la riproducibilità attraverso i seguenti principi:
* **Ambienti Effimeri e Isolati:** Ogni job viene eseguito all'interno di una macchina virtuale standardizzata e pulita (`runs-on: ubuntu-latest`), eliminando il problema delle dipendenze locali o del "works on my machine".
* **Immutabilità:** L'artefatto viene compilato una sola volta in un ambiente controllato.
* **Versionamento delle Actions:** L'uso di versioni pinnate per le actions (es. `@v4`) assicura che aggiornamenti imprevisti degli strumenti non rompano improvvisamente la build.

## 3. Garanzia della Qualità del Codice (Code Quality)
* **Automated Verification:** L'esecuzione automatica della suite di test (`mvn test`) ad ogni commit o pull request funge da gate di qualità. Questo riduce drasticamente il *mean time to detect* (MTTD), prevenendo l'integrazione di bug di regressione nel branch principale e fornendo un feedback loop rapido agli sviluppatori.