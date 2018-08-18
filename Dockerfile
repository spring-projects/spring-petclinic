FROM alpine/git
LABEL maintainer="mrcool435@gmail.com"
WORKDIR /app
RUN git clone https://github.com/Sanjeev435/spring-petclinic.git

FROM maven:3.5.4-jdk-8
WORKDIR /app
COPY --from=0 /app/spring-petclinic /app
RUN mvn install -Dmaven.test.skip=true

FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY --from=1 /app/target/spring-petclinic-1.5.1.jar /app
CMD ["java","-jar","spring-petclinic-1.5.1.jar"]
