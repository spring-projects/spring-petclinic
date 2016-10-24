FROM openjdk:alpine
MAINTAINER Antoine Rey <antoine.rey@free.fr>
# Spring Boot application creates working directories for Tomcat by default
VOLUME /tmp
ADD petclinic.jar petclinic.jar
RUN sh -c 'touch /petclinic.jar'
# To reduce Tomcat startup time we added a system property pointing to "/dev/urandom" as a source of entropy.
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/petclinic.jar"]
