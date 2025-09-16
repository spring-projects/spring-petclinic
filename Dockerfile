FROM maven: 3.9.10-eclipse-temurin-17 AS build
RUN git clone https://github.com/qadevopsgdr/spring-petclinic.git

RUN cd spring-petclinic && mvn clean package

FROM amazoncorretto:17-alpine-jdk
RUN mkdir /spc && chown nobody /spc
# switch the user
USER nobody

WORKDIR /spc
COPY --from=build --chown=nobody /spring-petclinic/target/spring-petclinic-3.5.0-SNAPSHOT.jar /spc/spring-petclinic.jar
EXPOSE 8080
CMD ["java","-jar","spring-petclinic.jar"]
