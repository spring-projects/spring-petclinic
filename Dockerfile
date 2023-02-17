FROM openjdk:19-buster

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve

COPY src ./src

CMD ["./mvnw", "spring-boot:run"]

############################Build the container############################
#docker build -t java-dcoker .
########To run and delete the container post shutdown######################
#docker run --rm -p 8080:8080 java-docker
