FROM eclipse-temurin:17-jdk-alpine@sha256:b16e661d76d3af0d226d0585063dbcafe7fb8a4ef31cfcaaec71d39c41269420
RUN apk add --no-cache iproute2
RUN mkdir /app
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser
COPY ./target/*.jar /app/java-application.jar
WORKDIR /app
RUN chown -R javauser:javauser /app
USER javauser
CMD ["java", "-jar", "java-application.jar"]