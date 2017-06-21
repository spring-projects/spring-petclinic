FROM maven:latest

# Set the workdir
WORKDIR /app

# Copy the source to the container
COPY . .

# Build the project
RUN mvn package -Dskip.failsafe.tests -q --batch-mode

# Make the jar executable
RUN sh -c 'touch target/spring-petclinic-*.jar'

#Start the project
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar target/spring-petclinic-*.jar" ]
