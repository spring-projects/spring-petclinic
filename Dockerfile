# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:17-jdk-jammy

# Set the working directory in the container
WORKDIR /app

# Copy the compiled JAR file from the build stage
COPY /target/*.jar /app/petclinic.jar
#COPY .mvn/ .mvn/
#COPY mvnw pom.xml ./
#Run the dependecies on the image
#RUN ./mvnw dependency:resolve

#COPY soruce code from local to the image
#COPY src ./src

# Specify the command to run on container start
#CMD ["mvnw", "spring-boot:run"]
CMD ["java","-jar","/app/petclinic.jar"]