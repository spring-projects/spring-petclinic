FROM java:8-jdk-alpine
RUN useradd -m -u 1000 -s /bin/bash jenkins
COPY /home/owlleg6/jenkins/workspace/builder-centos/spring-petclinic/target/spring-petclinic-2.4.2.jar /home/spring-petclinic-2.4.2.jar
CMD ["java","-jar","/home/spring-petclinic-2.4.2.jar"]
