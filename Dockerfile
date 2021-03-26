FROM java:8-jdk-alpine
ADD ./target/*.jar /usr/app/petclinic.jar
WORKDIR	/usr/app
CMD ["java","-jar","petclinic.jar"]
