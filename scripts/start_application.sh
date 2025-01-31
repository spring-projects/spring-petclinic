#!/bin/bash
echo "Navigating to the application directory..."
cd /home/ec2-user/spring-petclinic

echo "Building the application using Maven..."
mvn clean package -DskipTests

echo "Starting the Spring Boot application..."
java -jar target/spring-petclinic-*.jar > app.log 2>&1 &
echo "Application started successfully!"

