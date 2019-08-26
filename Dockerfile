FROM openjdk:8
COPY ${JAR_FILE_PATH} /var/lib/jenkins/workspace/sample/target/petclinic.jar
ENTRYPOINT java -jar /opt/spring-petclinic/petclinic.jar
EXPOSE 8080
