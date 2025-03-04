FROM maven AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

RUN jar xf /app/target/spring-petclinic-3.4.0-SNAPSHOT.jar
RUN jdeps \
    --ignore-missing-deps \
    --print-module-deps \
    --multi-release 17 \
    --recursive \
    --class-path 'BOOT-INF/lib/*' \
    /app/target/spring-petclinic-3.4.0-SNAPSHOT.jar > modules.txt

RUN $JAVA_HOME/bin/jlink \
         --add-modules $(cat modules.txt) \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /javaruntime

FROM debian:buster-slim 
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"
COPY --from=build /javaruntime $JAVA_HOME

WORKDIR /app
COPY --from=build /app/target/spring-petclinic-3.4.0-SNAPSHOT.jar .
CMD ["java", "-Dspring.profiles.active=postgres", "-jar", "spring-petclinic-3.4.0-SNAPSHOT.jar"]

