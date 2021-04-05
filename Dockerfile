FROM openjdk:8

ADD spring-petclinic-2.4.2.jar docker-spring-petclinic-boot.jar

EXPOSE 8081

ENTRYPOINT [ "java" , "-jar",  "docker-spring-petclinic-boot.jar" ]

