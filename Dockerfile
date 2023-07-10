FROM eclipse-temurin:17.0.6_10-jre-alpine

RUN mkdir /opt/app
COPY ./build/*.jar /opt/app/app.jar

CMD ["java", "-jar", "/opt/app/app.jar"]
