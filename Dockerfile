FROM maven:3.9.6-amazoncorretto-17-al2023@sha256:665ce50a354231b6c2e713f0d960814bdbe498adf143f7f477778c1a18c285a7 AS build

RUN mkdir /project
COPY . /project
WORKDIR /project

RUN mvn clean -DskipTests -Darguments=-DskipTests package
RUN mv /project/target/spring-petclinic-*.jar /project/target/java-application.jar


FROM gcr.io/distroless/java17@sha256:e7ff67bc21960968a6e022de286d35c49bf68db728d76d1a8a533bc08ceb36c2

EXPOSE 8080
WORKDIR /app

COPY --from=build /project/target/java-application.jar /app/java-application.jar

ENTRYPOINT [ "java", "-Dspring.profiles.active=postgres", "-jar", "java-application.jar"]
