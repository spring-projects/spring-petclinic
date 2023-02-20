FROM openjdk:21-jdk

# Set the working directory to /app
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Make port 8080 available to the world outside this container
EXPOSE 8080

RUN ./mvnw package

CMD ["java", "-jar", "target/spring-petclinic-3.0.0-SNAPSHOT.jar"]
