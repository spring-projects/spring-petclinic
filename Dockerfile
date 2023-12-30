# Use a base image with Java runtime
FROM amazoncorretto:17-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled JAR file into the container at /app
COPY target/spring-petclinic-*.jar /app/app.jar

# Expose the port that your application will run on
EXPOSE 8080

# Specify the command to run your application
CMD ["java", "-jar", "app.jar"]
