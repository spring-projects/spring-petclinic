FROM tomcat:8

EXPOSE 8080

RUN mkdir /apps/

COPY ./spring-petclinic-2.1.0.BUILD-SNAPSHOT.jar /apps/app.jar

CMD java -jar /apps/app.jar
