FROM alpine/git AS clone
WORKDIR /app
RUN git clone https://github.com/secobau/spring-petclinic.git 

FROM maven:alpine AS build
WORKDIR /app
COPY --from=clone /app/spring-petclinic /app 
RUN mvn install && mv target/spring-petclinic-*.jar target/spring-petclinic.jar

FROM openjdk:jre-alpine AS production
WORKDIR /app
COPY --from=build /app/target/spring-petclinic.jar /app
ENTRYPOINT ["java","-jar"]
CMD ["spring-petclinic.jar"]
