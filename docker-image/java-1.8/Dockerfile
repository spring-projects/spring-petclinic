FROM registry.redhat.io/ubi8/ubi
WORKDIR /app
RUN useradd appuser && \
    chown -R appuser:appuser /app && \
    chgrp -R 0 /app && \
    chmod -R g=u /app
RUN yum install -y git java-1.8.0-openjdk && yum clean all -y
USER maven
