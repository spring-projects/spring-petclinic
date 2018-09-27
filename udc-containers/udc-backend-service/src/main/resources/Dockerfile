FROM openjdk:8-jre

ENV LANG=en_US.UTF-8 \
    LANGUAGE=en_US.UTF-8 \
    LC_ALL=en_US.UTF-8

ENV BASE_DIR='/udc/apps/petclinic'

EXPOSE 8080
ENTRYPOINT ["/entrypoint.sh"]

RUN mkdir -p ${BASE_DIR}

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

COPY todo.jar /udc/apps/petclinic/todo.jar

LABEL project.groupid="${project.groupId}" \
      project.artifactid="${project.artifactId}" \
      project.version="${project.version}"
