FROM openjdk:12-jdk-alpine
COPY springbootify.jar springbootify.jar
CMD ["java","-jar","springbootify.jar"]