FROM gradle:8.5-jdk17 as builder
COPY . /app
WORKDIR /app
RUN gradle build -x test

FROM eclipse-temurin:17-jre-alpine
WORKDIR/app
COPY --from=builder /app/build/libs/spring-petclinic-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
