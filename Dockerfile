FROM ubuntu:22.04
RUN apt update && apt install git -y
RUN apt-get install openjdk-17-jdk -y
RUN git clone https://github.com/Cloud-and-devops-notes/spring-petclinic-jenkins.git
RUN cd spring-petclinic-jenkins
CMD ["sleep","1d"]