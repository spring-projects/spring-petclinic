FROM maven:3.9.9-eclipse-temurin-17-alpine AS builder

WORKDIR /petclinic-app
COPY pom.xml ./

RUN mvn dependency:go-offline -B

COPY src/ ./src/
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine AS jre-builder
WORKDIR /app

COPY --from=builder /petclinic-app/target/*.jar /app/petclinic.jar

EXPOSE 8080

CMD [ "java", "-jar", "/app/petclinic.jar" ]

# ||| potential upgrade:
# adding user
# .dockerignore don't need -> COPY only what needed
# mount .m2 repository


