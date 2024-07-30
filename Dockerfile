FROM amazoncorretto:17-alpine

WORKDIR /app

COPY build/libs/spring-petclinic-3.3.0.jar .

CMD [ "java", "-jar", "spring-petclinic-3.3.0.jar" ]
