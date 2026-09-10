
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn package -DskipTests


FROM eclipse-temurin:17-jre-jammy

RUN addgroup --system spring && adduser --system --ingroup spring spring

WORKDIR /app

COPY --from=builder --chown=spring:spring /build/target/spring-petclinic-*.jar app.jar

USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]