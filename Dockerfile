FROM openjdk:8
COPY /var/lib/jenkins/workspace/sample/target/petclinic.jar /opt/petclinic.ja
ENTRYPOINT java -jar /opt/petclinic.jar
EXPOSE 8080
