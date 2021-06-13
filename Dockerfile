FROM node:anapsix/alpine-java
WORKDIR /usr/src/app
COPY package*.jar ./
RUN mvn install
COPY . .
EXPOSE 8080
CMD [ "java", "-jar target/*.jar" ]

 
