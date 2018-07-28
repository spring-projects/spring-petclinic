FROM maven:3.5-jdk-8-alpine
COPY . /src
WORKDIR /src
RUN mvn install

FROM openjdk:8-jre-alpine
WORKDIR /app
EXPOSE 8080
ENV VERSION=2.0.0.BUILD-SNAPSHOT
COPY --from=0 /src/target/spring-petclinic-$VERSION.jar /app/spring-petclinic.jar
CMD ["java", "-jar", "spring-petclinic.jar"]
