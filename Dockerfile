FROM openjdk:8-jdk-alpine

COPY ../spring-petclinic/target/spring-petclinic-2.4.5.jar  app.jar

ENV JVM_OPTS="-Xmx1024m -Xms512m"

CMD  java $JVM_OPTS  -jar app.jar

EXPOSE 8088

