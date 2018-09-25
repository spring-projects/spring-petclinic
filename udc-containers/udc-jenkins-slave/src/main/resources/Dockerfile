FROM jenkinsci/jnlp-slave

ENV CLOUDSDK_CORE_DISABLE_PROMPTS 1
ENV PATH /opt/google-cloud-sdk/bin:${PATH}

USER root

RUN apt-get update -y
RUN apt-get install -y jq \
      unzip \
      maven \
      apt-transport-https \
      ca-certificates \
      gnupg2 \
      software-properties-common

RUN curl -fsSL https://download.docker.com/linux/$(. /etc/os-release; echo "${ID}")/gpg > /tmp/dkey; apt-key add /tmp/dkey \
 && add-apt-repository \
    "deb [arch=amd64] https://download.docker.com/linux/$(. /etc/os-release; echo "${ID}") \
    $(lsb_release -cs) \
    stable" \
 && apt-get update \
 && apt-get -y install docker-ce

RUN curl https://sdk.cloud.google.com | bash && mv google-cloud-sdk /opt
RUN gcloud components install kubectl
RUN curl -sfSL https://raw.githubusercontent.com/kubernetes/helm/master/scripts/get | bash

USER jenkins
