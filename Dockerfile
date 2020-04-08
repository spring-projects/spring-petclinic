from openjdk
COPY /target/spring*.jar pet-animal/
EXPOSE 8080
CMD ["java","-jar","pet-animal/spring-petclinic-2.2.0.BUILD-SNAPSHOT"]
