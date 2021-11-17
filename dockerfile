# Arguments:
# project = project name
# artifactId
# VERSION

# Build stage
FROM maven:3.5-jdk-8-alpine as build
ARG project
WORKDIR /home/ec2-user/workspace/EPAM_Final_Project/pipeline_3_jenkinsfile/
COPY . /home/ec2-user/workspace/EPAM_Final_Project/pipeline_3_jenkinsfile/
RUN mvn install

# Run stage
FROM openjdk:8-jre-alpine
#ARG ARTIFACT_ID
#ARG VERSION
ARG PORT=8080
#ENV ARTIFACT ${ARTIFACT_ID}-${VERSION}.jar
WORKDIR /home/ec2-user/workspace/EPAM_Final_Project/pipeline_3_jenkinsfile/
COPY --from=build /home/ec2-user/workspace/EPAM_Final_Project/pipeline_3_jenkinsfile/target/*.jar /home/ec2-user/workspace/EPAM_Final_Project/pipeline_3_jenkinsfile/
EXPOSE $PORT
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar ${ARTIFACT} --server.port=${PORT}"]