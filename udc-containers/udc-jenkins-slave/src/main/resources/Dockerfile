FROM gcr.io/cloud-solutions-images/jenkins-k8s-slave

RUN apt-get update -y
RUN apt-get install -y jq \
      unzip \
      maven

RUN curl -sfSL https://raw.githubusercontent.com/kubernetes/helm/master/scripts/get | bash
