FROM jenkins/jenkins:latest
USER root

COPY scripts/install-awscli.sh /install-awscli.sh
RUN chmod u+x /install-awscli.sh && \
    /install-awscli.sh

COPY scripts/install-docker.sh /install-docker.sh
RUN chmod u+x /install-docker.sh && \
    /install-docker.sh

COPY scripts/install-docker-compose.sh /install-docker-compose.sh
RUN chmod u+x /install-docker-compose.sh && \
    /install-docker-compose.sh