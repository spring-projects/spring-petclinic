FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY ./spring-petclinic/ .

RUN mvn package -DskipTests -Dcheckstyle.skip=true

#------------------------------

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/spring-petclinic-*.jar /app.jar

# EXPOSE 8080

CMD ["java", "-jar", "/app.jar"]