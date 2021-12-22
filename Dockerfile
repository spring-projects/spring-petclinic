FROM openjdk:11.0.1-jre-slim-stretch
EXPOSE 8085
ARG JAR=spring-petclinic-2.5.0-SNAPSHOT.jar
COPY org.cts/spring-petclinic/2.5.0-SNAPSHOT/$JAR /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
