FROM openjdk:8

ARG JAR_FILE=JAR_FILE_MUST_BE_SPECIFIED_AS_BUILD_ARG

COPY ${JAR_FILE} docker-spring-petclinic-boot.jar

#ADD /workspace/target/spring-petclinic-2.4.2.jar docker-spring-petclinic-boot.jar

EXPOSE 80

ENTRYPOINT [ "java" , "-jar",  "docker-spring-petclinic-boot.jar" ]

