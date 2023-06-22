FROM openjdk:17.0.1-slim
WORKDIR /opt
ENV PORT 8080
ENV POSTGRES_USER petclinic
ENV POSTGRES_PASSWORD petclinic
ENV POSTGRES_URL jdbc:postgresql://postgres/petclinic
ENV JAVA_OPTS "-Dspring.profiles.active=postgres -Xmx2g"
EXPOSE 8080
COPY target/*.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
