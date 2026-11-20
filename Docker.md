# Report Docker - Spring PetClinic

## 1. Containerizzazione (`Dockerfile`)
È stato creato un `Dockerfile` per eseguire Spring PetClinic in un container Docker utilizzando Java 17. L'applicazione viene compilata con Maven e avviata tramite un file `.jar`.

Sono state adottate alcune buone pratiche:
* **Sicurezza:** Esecuzione dell'applicazione con un utente non-root e nessuna credenziale inserita nel Dockerfile.
* **Riproducibilità:** Utilizzo di un'immagine Java con versione specifica, evitando `latest`.
* **Ottimizzazione:** Utilizzo di `COPY` invece di `ADD`, `ENTRYPOINT` e `.dockerignore` per escludere file non necessari.

L'immagine è stata costruita e testata correttamente in un container sulla porta 8080.

## 2. Docker Compose e Affidabilità
Attraverso `docker-compose.yml` sono stati configurati due servizi separati: **Web** (Spring PetClinic) e **Postgres** (database PostgreSQL 18.4).

Sono stati implementati:
* **Healthcheck:** Controllano lo stato dei servizi e permettono all'applicazione di attendere che il database sia disponibile.
* **Restart Policy:** `restart: on-failure` permette il riavvio dei container in caso di errore del processo.
* **Persistenza:** Il volume `postgres_data` conserva i dati anche dopo la rimozione dei container.
* **Sicurezza:** Le credenziali sono gestite tramite `.env`, escluso da Git.

Sono stati inoltre eseguiti tre test:
* **Fault Injection:** Arrestando PostgreSQL, l'applicazione ha mostrato errori di connessione e timeout.
* **Recovery:** Riavviando il database, l'applicazione è tornata a funzionare normalmente.
* **Data Persistence:** Dopo aver eliminato e ricreato i container, i dati inseriti erano ancora presenti.

## 3. Integrazione CI/CD e DockerHub
È stato creato il workflow `.github/workflows/docker-build.yml` per automatizzare la costruzione e pubblicazione delle immagini Docker.

Dall'analisi del workflow di riferimento e della configurazione realizzata:
* **Trigger:** `workflow_run` avvia la fase Docker solo dopo il successo della pipeline CI/CD del Lab1 su `main`.
* **Secrets Management:** Le credenziali DockerHub sono protette tramite GitHub Secrets.
* **Multi-Platform:** Le immagini vengono generate per `linux/amd64` e `linux/arm64`.
* **Caching:** Le cache Maven e Docker riducono i tempi delle build successive.
* **Metadata:** I tag `latest` e SHA permettono di identificare le immagini e collegarle ai commit.

L'integrazione permette di automatizzare la distribuzione delle immagini, migliorando riproducibilità, sicurezza e tracciabilità del progetto.