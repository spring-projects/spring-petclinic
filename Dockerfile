# syntax=docker/dockerfile:1

# ---- Stage 1: build ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
# Le cache Maven survit entre les builds: le 2e build est bien plus rapide
RUN --mount=type=cache,target=/root/.m2 mvn -B -DskipTests package

# ---- Stage 2: runtime ----
FROM eclipse-temurin:21-jre
WORKDIR /app
# Le joker évite d'écrire la version en dur (le .jar.original n'est pas concerné)
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8080
# Réglage de la JVM pour un conteneur: elle utilise 75 % de la mémoire allouée
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"
# Exécution sans les droits root
USER 10001:10001
ENTRYPOINT ["java", "-jar", "app.jar"]