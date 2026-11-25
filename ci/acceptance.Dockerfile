# Dockerfile PROVISÓRIO, usado só pelo teste de aceitação (ci/acceptance.sh).
# Substituir pelo Dockerfile da P5 quando ele existir.
# Contexto do build: target/
FROM eclipse-temurin:17-jre
COPY spring-petclinic-*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
