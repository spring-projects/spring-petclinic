FROM openjdk:11
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
CMD ["java", "-jar", "main.jar"]
