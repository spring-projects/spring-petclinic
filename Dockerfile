FROM maven:3.8.1-adoptopenjdk-11 as mvn-package
RUN echo "${PWD}"  \
    && mkdir "${PWD}/app"
COPY ${WORKSPACE}/* ${PWD}/app
RUN mvn package

FROM openjdk:11
ARG TOMCAT_PORT
RUN echo "${PWD}"  \
    && mkdir "${PWD}/app"
COPY --from=mvn-package ./target/*.jar ${PWD}/app
CMD ["java", "-jar", "${PWD}/app/*.jar"]
EXPOSE ${TOMCAT_PORT}
