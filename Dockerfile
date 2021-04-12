FROM openjdk:8

ADD target/spring-petclinic-2.4.2.jar docker-spring-petclinic-boot.jar

EXPOSE 8080

ENTRYPOINT [ "java" , "-jar",  "docker-spring-petclinic-boot.jar" ]

