FROM anapsix/alpine-java
COPY /target/*.jar /home/app.jar
CMD ["java","-jar","/home/app.jar"]