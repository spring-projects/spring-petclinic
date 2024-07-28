# Use an official OpenJDK runtime as a parent image
FROM openjdk

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and the pom.xml file
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Copy the project source code
COPY src ./src

# Introduce an ARG to act as a cache breaker
ARG CACHEBUSTER=unknown

# Package the application
RUN ./mvnw clean package -Dmaven.test.skip=true
RUN ls -alh /app/target/

# Move the JAR file to the app directory (update this if necessary based on actual JAR names)
COPY target/*.jar /app/app.jar

# Run the jar file
CMD ["java", "-jar", "app.jar"]

# Expose the port the app runs on
EXPOSE 8080
