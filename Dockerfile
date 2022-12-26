FROM amazoncorretto:11
LABEL Version="1.0"
LABEL Authour="raakhi"
LABEL Description="This-is-for-practice"
ADD https://github.com/peddiraju3122b/spring-petclinic.git /home/ubuntu/spring-petclinic/Dockerfile 
EXPOSE 8080
CMD ["java","-jar","/spring-petclinic-2.4.2.jar"]