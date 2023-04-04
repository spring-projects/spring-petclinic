FROM openjdk:18
ARG DATABASE
WORKDIR app
COPY . .
RUN /app/mvnw package -DskipTests
EXPOSE 8080
ENV DATABASE ${DATABASE}
CMD sh -c 'java -jar -Dspring.profiles.active=$DATABASE /app/target/*.jar'
