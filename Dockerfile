FROM java:8-jre

ADD ./target/pet-clinic.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/pet-clinic.jar"]

EXPOSE 8080
