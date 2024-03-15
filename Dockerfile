FROM eclipse-temurin
CMD ["./mvnw","clean","package"]
ARG JAR_FILE_PATH=target/*.jar
COPY ${JAR_FILE_PATH} spring-petclinic.jar
ENTRYPOINT ["java","-jar", "spring-petclinic.jar"]
