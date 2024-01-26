FROM openjdk

WORKDIR /app

COPY target/spring-petclinic-*.jar /app/spring-petclinic.jar

EXPOSE 8080

CMD ["java", "-jar", "spring-petclinic.jar"]
