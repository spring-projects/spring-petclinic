FROM maven:latest

# Set the workdir
WORKDIR /app

# Copy the source to the container
COPY . .

# Build the project
RUN mvn package -Dskip.failsafe.tests -q --batch-mode

# Copy and make the jar executable
RUN sh -c 'mkdir dist/ && cp -a target/. dist/ && touch dist/spring-petclinic-*.jar'

#Start the project
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar dist/spring-petclinic-*.jar" ]
