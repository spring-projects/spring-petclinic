FROM openjdk:17-jdk-slim
WORKDIR /root
COPY target/*.jar app.jar
EXPOSE 8086
CMD ["sh", "-c", "java -jar /root/app.jar & sleep 3600"]
