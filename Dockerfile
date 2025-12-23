FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /sujith
RUN git clone https://github.com/spring-projects/spring-petclinic.git
RUN cd /sujith/spring-petclinic && mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk 
COPY --from=builder /sujith/spring-petclinic/target/*.jar /sujith/app.jar
EXPOSE 8080
CMD ["java", "-jar", "/sujith/app.jar"]
