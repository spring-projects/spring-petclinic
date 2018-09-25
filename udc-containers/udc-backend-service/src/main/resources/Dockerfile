FROM ${docker.baseImage}

EXPOSE 8080
ENTRYPOINT ["/docker-entrypoint.sh"]
WORKDIR "/udc/apps/petclinic"

COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

COPY todo.jar /udc/apps/petclinic/todo.jar

LABEL project.groupid="${project.groupId}" \
      project.artifactid="${project.artifactId}" \
      project.version="${project.version}"
