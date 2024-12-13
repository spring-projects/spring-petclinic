FROM maven:3.9-eclipse-temurin-17 AS builder
# build the java code
COPY . /spc
WORKDIR /spc
RUN mvn package
# this will create a spring petclinic jar file


FROM eclipse-temurin:17-jre AS runner
COPY --from=builder --chown=ubuntu /spc/target/spring-petclinic-3.3.0-SNAPSHOT.jar /app/spring-petclinic.jar
USER ubuntu
WORKDIR /app
EXPOSE 8080
CMD ["java", "-jar", "spring-petclinic.jar"]