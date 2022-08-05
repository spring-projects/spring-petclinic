FROM maven:3.8.1-adoptopenjdk-11 as mvn-package
COPY ./* ./
RUN mvn package

FROM openjdk
ARG TOMCAT_PORT
RUN mkdir /app
COPY --from=mvn-package ./target/*.jar ./
WORKDIR ./
CMD java -jar *.jar
EXPOSE ${TOMCAT_PORT}
