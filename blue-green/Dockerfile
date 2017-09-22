FROM maven:3.5.0

RUN apt-get update -y && apt-get install -y awscli jq

# Jenkins workspace runs with UID 1000
RUN useradd -ms /bin/bash -u 1000 jenkins
USER jenkins
