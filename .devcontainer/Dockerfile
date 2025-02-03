# Build stage
FROM registry.access.redhat.com/ubi8/openjdk-17:latest as builder

# Install required packages
USER root
RUN microdnf install -y tar gzip

# Switch back to default user
USER 1001

WORKDIR /app

# Copy Maven wrapper and POM
COPY --chown=1001:0 .mvn/ .mvn/
COPY --chown=1001:0 mvnw pom.xml ./
RUN chmod +x mvnw

# Create Checkstyle suppressions file
RUN echo '<?xml version="1.0"?>\n\
<!DOCTYPE suppressions PUBLIC "-//Checkstyle//DTD SuppressionFilter Configuration 1.2//EN" "https://checkstyle.org/dtds/suppressions_1_2.dtd">\n\
<suppressions>\n\
    <suppress files=".*\.jar" checks="NoHttp"/>\n\
    <suppress files=".*\.pom" checks="NoHttp"/>\n\
</suppressions>' > checkstyle-suppressions.xml

# Download dependencies
RUN ./mvnw dependency:go-offline -Dcheckstyle.suppressions.location=checkstyle-suppressions.xml

# Copy source code
COPY --chown=1001:0 src ./src

# Build the application
RUN ./mvnw package -DskipTests -Dcheckstyle.suppressions.location=checkstyle-suppressions.xml

# Run stage
FROM registry.access.redhat.com/ubi8/openjdk-17-runtime:latest

WORKDIR /app

# Copy the built artifact from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Container runs on port 8080
EXPOSE 8080

# Set the startup command
ENTRYPOINT ["java", "-jar", "app.jar"]
