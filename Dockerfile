# Use an OpenJDK base image
FROM openjdk:17-jdk-alpine

# Setting the working directory in the container
WORKDIR /app/petclinic

# Copy the JAR file from the host machine to the container
COPY target/spring-petclinic-3.3.0-SNAPSHOT.jar .

# Expose the port your application runs on
EXPOSE 8888

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "spring-petclinic-3.3.0-SNAPSHOT.jar"]
