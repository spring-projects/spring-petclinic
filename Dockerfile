FROM openjdk:8
COPY ./target/petclinic.war /opt/petclinic.war
ENTRYPOINT java -jar /opt/petclinic.war
EXPOSE 9090
