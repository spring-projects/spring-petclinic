FROM jenkins/jenkins:lts-jdk21

USER root

COPY --from=docker:27-cli /usr/local/bin/docker /usr/local/bin/docker
COPY --chown=jenkins:jenkins plugins.txt /usr/share/jenkins/ref/plugins.txt

RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

USER jenkins
