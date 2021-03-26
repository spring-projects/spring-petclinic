FROM java:8-jdk-alpine
ADD ./target/*.jar /usr/app/petclinic.jar
WORKDIR	/usr/app
ENTRYPOINT ["java","-jar","petclinic.jar"]
