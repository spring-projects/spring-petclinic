# Example of custom Java runtime using jlink in a multi-stage container build
FROM gradle:jdk21 as jdk-build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN ls -al
RUN gradle build -x test --no-daemon 

# Create a custom Java runtime
# RUN $JAVA_HOME/bin/jlink \
#          --add-modules java.base \
#          --strip-debug \
#          --no-man-pages \
#          --no-header-files \
#          --compress=2 \
#          --output /javaruntime

FROM openjdk:21 
EXPOSE 8080

RUN mkdir /app

# ENV SPRING_PROFILES_ACTIVE=dev

COPY --from=jdk-build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar

ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions",  "-Djava.security.egd=file:/dev/./urandom","-jar","/app/spring-boot-application.jar", "$JAVA_OPTS"]