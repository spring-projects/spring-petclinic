FROM openjdk:17-jdk

WORKDIR /app

COPY build/libs/spring-petclinic-3.2.0.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java"]

CMD ["-jar", "app.jar"]
