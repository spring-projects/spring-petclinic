FROM maven:3-amazoncorretto-17 AS build
ADD . /spring-petclinic
WORKDIR /sprig-petclinic
RUN mvn package



FROM amazoncorretto:17-alpine-jdk
LABEL project="spc"
COPY --from=build /springpetclinic/target/spring-petclinic-3.2.0-SNAPSHOT.jar /spring-petclinic-3.2.0-SNAPSHOT.jar
CMD [ "jav", "-jar", "/spring-petclinic-3.2.0-SNAPSHOT.jar" ]
