FROM mcr.microsoft.com/vscode/devcontainers/java:0-17-bullseye

WORKDIR /workspace

ARG NODE_VERSION="none"
RUN if [ "${NODE_VERSION}" != "none" ]; then su vscode -c "umask 0002 && . /usr/local/share/nvm/nvm.sh && nvm install ${NODE_VERSION} 2>&1"; fi

ARG JAVA_VERSION=17.0.7-ms
RUN ln -s bash /bin/sh.bash
RUN mv /bin/sh.bash /bin/sh
RUN bash -lc '. /usr/local/sdkman/bin/sdkman-init.sh && sdk install java $JAVA_VERSION && sdk use java $JAVA_VERSION'

