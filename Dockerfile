FROM mcr.microsoft.com/java/maven:8-zulu-debian10

RUN sudo yum update -y

WORKDIR /opt

COPY **/*.jar /opt/apache-tomcat-9.0.35/webapps/host-manager/WEB-INF/lib

COPY org.springframework.samples/spring-petclinic/2.4.2/spring-petclinic-2.4.2.jar /opt/apache-tomcat-9.0.35/webapps/host-manager/WEB-INF/lib

RUN ./mvnw spring-boot:run

EXPOSE 8081

RUN ./mvnw package 

CMD [ 'java', '-jar target/*.jar' ]

