FROM openjdk:11
LABEL author="Shaik Naseer"
LABEL Organization="manatech.store"
EXPOSE 8080
ADD https://referenceapplicationskhaja.s3.us-west-2.amazonaws.com/spring-petclinic-2.4.2.jar /spring-petclinic.jar
CMD ["java","-jar", "/spring-petclinic.jar"]