FROM java:8-jdk-alpine
ADD ./target/*.jar /usr/app/petclinic.jar
WORKDIR	/usr/app
EXPOSE 8080
ENTRYPOINT ["java","-jar","petclinic.jar"]
