FROM ubuntu:latest
LABEL author="Sridhar" organization="SMSK"
RUN apt update && apt install openjdk-17-jdk -y
RUN apt install wget -y && cd /tmp
RUN wget https://dlcdn.apache.org/maven/maven-3/3.9.4/binaries/apache-maven-3.9.4-bin.tar.gz
RUN mkdir /usr/share/maven
RUN tar -xvzf apache-maven-3.9.4-bin.tar.gz -C /usr/share/maven
ENV PATH="$PATH:/usr/share/maven/apache-maven-3.9.4/bin"
RUN mvn --version
RUN apt install git -y
RUN git clone https://github.com/spring-projects/spring-petclinic.git
RUN cd spring-petclinic && mvn package
RUN ls
EXPOSE 8080
CMD ["java", "-jar", "/spring-petclinic/target/spring-petclinic-3.1.0-SNAPSHOT.jar"]