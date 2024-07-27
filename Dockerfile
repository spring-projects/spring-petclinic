# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and the pom.xml file
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Copy the project source code
COPY src ./src

RUN chmod +x ./mvnw
# Package the application
RUN ./mvnw clean package -DskipTests -X

# Copy the JAR file to the app directory
COPY target/*.jar app.jar

# Run the jar file
CMD ["java", "-jar", "app.jar"]

# Expose the port the app runs on
EXPOSE 8080
