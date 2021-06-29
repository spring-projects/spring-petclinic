FROM openjdk:11-jre

ARG JAR_FILE=./target/*.jar
ENV XMS=100m
ENV XMX=100m

RUN addgroup --system petclinic && adduser --system --group petclinic
USER petclinic:petclinic

COPY ${JAR_FILE} application.jar

CMD java -jar -Xms${XMS} -Xmx${XMX} application.jar

# final container start command
# docker container run -p 8080:8081 -v "$(pwd)/local.properties:/application.properties" -e "XMS=200m" -e "XMX=200m" spring-petclinic

