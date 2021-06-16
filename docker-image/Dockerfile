FROM quay.io/yakiyoshi/spring-petclinic:maven-3.5.4-java-1.8
RUN git clone https://github.com/yakiyoshi/spring-petclinic.git
WORKDIR /opt/maven/spring-petclinic
RUN ./mvnw package

FROM registry.redhat.io/ubi8/ubi
ARG JAR_FILE=target/*.jar
RUN yum install -y java-11-openjdk && yum clean all -y
RUN mkdir -p /opt/app
COPY ${JAR_FILE} /opt/app/app.jar
RUN useradd spring && \
    chown -R spring:spring /opt/app
RUN chgrp -R 0 /opt/app && \
    chmod -R g=u /opt/app
USER spring:spring
ENTRYPOINT ["java","-jar","/opt/app/app.jar"]
EXPOSE 8080
