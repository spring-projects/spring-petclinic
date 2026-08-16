# Première étape : construire l'application
FROM maven:3.9.16-eclipse-temurin-17-noble AS build

WORKDIR /workspace

# Copier d'abord le POM permet de mettre les dépendances en cache
COPY pom.xml .

RUN mvn --batch-mode --no-transfer-progress dependency:go-offline

# Copier ensuite le code source
COPY src ./src

# Construire le JAR
RUN mvn --batch-mode --no-transfer-progress clean package -DskipTests


# Deuxième étape : exécuter l'application
FROM eclipse-temurin:17-jre-noble AS runtime

# Installer curl pour le healthcheck et créer un utilisateur non-root
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd --gid 10001 petclinic \
    && useradd --uid 10001 --gid petclinic --no-create-home \
       --shell /usr/sbin/nologin petclinic

WORKDIR /app

# Récupérer uniquement le JAR produit dans la première étape
COPY --from=build --chown=petclinic:petclinic \
    /workspace/target/spring-petclinic-*.jar /app/app.jar

USER petclinic

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
    CMD curl --fail --silent --show-error \
    http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]