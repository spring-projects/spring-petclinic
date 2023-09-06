FROM ubuntu:22.04
RUN apt update && apt install git -y
RUN apt-get install nginx -y
# RUN git clone https://github.com/Cloud-and-devops-notes/spring-petclinic-jenkins.git
# RUN cd spring-petclinic-jenkins
EXPOSE 80
CMD ["sleep","1d"]