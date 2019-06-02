# Pull base image.
FROM ubuntu:latest

RUN \
    # Update
    apt-get update -y && \
    # Install Java
    apt-get install default-jre -y


EXPOSE 8080

CMD java -jar spring-petclinic.jar

# container registry
# mbankpoc
