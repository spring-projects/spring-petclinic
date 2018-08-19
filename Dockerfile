FROM alpine/git as clone

WORKDIR /app
RUN echo "Switch started"
RUN git clone https://github.com/Sanjeev435/spring-petclinic.git

FROM maven:3.5.4-jdk-8 as build
WORKDIR /app
COPY --from=clone /app/spring-petclinic /app
RUN mvn install -Dmaven.test.skip=true

FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/spring-petclinic-1.5.1.jar /app
CMD ["java","-jar","spring-petclinic-1.5.1.jar"]
