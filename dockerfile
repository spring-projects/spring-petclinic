FROM openjdk-11-jdk
ADD target/*.jar /
EXPOSE 8080
ENTRYPOINT ["java","-jar","/*.jar"]
