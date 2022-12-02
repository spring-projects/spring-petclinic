#stage-1
FROM maven:3.8-openjdk-18 as build
RUN git clone https://github.com/gopivurata/spring-petclinic.git && \
    cd spring-petclinic && \
    mvn package
