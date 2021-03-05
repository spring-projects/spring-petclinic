FROM spring-petclinic-2.4.2.jar

COPY target/spring-petclinic-1.5.1.jar /opt/spring-petclinic.jar

EXPOSE 8080
CMD ["java", "-jar", "/opt/spring-petclinic.jar"]
