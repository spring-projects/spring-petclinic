FROM @docker.baseImage@

ENV LANG=en_US.UTF-8 \
    LANGUAGE=en_US.UTF-8 \
    LC_ALL=en_US.UTF-8

ENV BASE_DIR='/udc/apps/petclinic'

EXPOSE 8080
ENTRYPOINT ["/entrypoint.sh"]

RUN mkdir -p ${BASE_DIR}

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

COPY udc-backend.jar ${BASE_DIR}/udc-backend-service.jar
