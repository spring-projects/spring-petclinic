FROM ubuntu:latest
RUN apt-get update
RUN mkdir test
COPY /.mvn/wrapper /test
RUN chmod 775 /test/.mvn/wrapper/
EXPOSE 8080
CMD ["java", "-jar", "/maven-wrapper.jar"]
