FROM openjdk:8-jdk-alpine AS	build
WORKDIR /build
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
RUN ./mvnw -B -e dependency:go-offline
COPY src src
RUN ./mvnw package

FROM openjdk:8-jre-alpine
EXPOSE 8080
ENTRYPOINT [ "java" ]
CMD [ "-jar", "-Dspring.profiles.active=mysql" ,"app.jar" ]
COPY --from=build /build/target/*.jar /app/app.jar
WORKDIR /app
