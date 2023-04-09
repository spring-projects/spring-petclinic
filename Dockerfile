FROM openjdk:11
ARG user=springpet
ARG group=springpet
ARG uid=1000
ARG gid=1000
LABEL author="Arjun"
LABEL project="test"
ADD https://arjunaartifact.jfrog.io/ui/native/libs-release-local/spring-petclinic-2.7.3.jar
EXPOSE 8080
RUN useradd springpet
USER ${user}
CMD [ "java", "-jar", "/spring-petclinic-2.7.3.jar" ]
