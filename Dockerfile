FROM openjdk:16-alpine3.13
WORKDIR /usr/src/app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY . .
EXPOSE 8081
CMD [ "java", "-jar target/*.jar" ]

 
