FROM openjdk:11
LABEL author="kanna"
copy target/*.jar /spring-petclinic.jar
CMD java -jar spring-petclinic.jar
