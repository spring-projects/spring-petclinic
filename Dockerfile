FROM openjdk:8
COPY /var/lib/jenkins/workspace/sample/target/petclinic.war /opt/petclinic.war
ENTRYPOINT java -jar /opt/petclinic.war
EXPOSE 8080
