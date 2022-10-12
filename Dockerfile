FROM openjdk:11
LABEL author="shaik"
LABEL author="manatesh.store"
EXPOSE 8080
ADD ttps://referenceapplicationskhaja.s3.us-west-2.amazonaws.com/spring-petclinic-2.4.2.jar /spring-petclinic.jar
CMD [ "java", "-jar", "/spring-petclinic.jar" ]
