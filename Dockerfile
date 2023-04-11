# Create Dockerfile for Spring-petclinic application using pre-built artifact

# FROM alpine:latest
# ENV JAR=spring-petclinic-3.0.3.jar
# RUN apk --no-cache add openjdk17-jre-headless
# COPY ./target/${JAR} ./
# EXPOSE 8080
# CMD ["/bin/sh", "-c", "/usr/bin/java -jar ${JAR}"]

# Create multi-stage Dockerfile for Spring-petclinic application

FROM alpine:latest AS builder
RUN apk --no-cache add openjdk17
WORKDIR /tmp
COPY ./ ./
RUN ./mvnw clean ; ./mvnw package

FROM alpine:latest
ENV DB=
RUN apk --no-cache add openjdk17-jre-headless
WORKDIR /home
COPY --from=builder /tmp/target/spring-*.jar  ./
EXPOSE 8080
CMD ["/bin/sh", "-c", "/usr/bin/java -jar /home/spring-*.jar ${DB}"]