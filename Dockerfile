
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Installa curl per gli healthcheck
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

# Crea un utente senza privilegi di root
RUN groupadd --system appgroup \
    && useradd --system --gid appgroup appuser

# Copia il JAR compilato
COPY --chown=appuser:appgroup target/spring-petclinic-*.jar app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
