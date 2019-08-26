FROM openjdk:8
COPY ./petclinic.war /opt/petclinic.war
ENTRYPOINT java -jar /opt/petclinic.war
EXPOSE 8080
