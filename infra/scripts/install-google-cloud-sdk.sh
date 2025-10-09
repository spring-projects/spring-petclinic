#!/usr/bin/env bash
set -e

usage()
{
    echo "usage: . install-google-cloud-sdk.sh
    [--with-key-file   local file path to service account json]

NOTE: requires 'dot' before install-google-cloud-sdk.sh
      so that the PATH variable  is exported succesfully to 
      the calling process, i.e. you don't need to provide
      full path to gcloud command after installation

      --with-key-file is optional, 
        if no authentication is required"
}

while [ "$1" != "" ]; do
  case "$1" in
      --with-key-file )     KEY_FILE="$2";    shift;;
      * )                   usage; exit 1
  esac
  shift
done

GOOGLE_CLOUD_SDK_ARCHIVE_URL=https://dl.google.com/dl/cloudsdk/channels/rapid/downloads/google-cloud-sdk-266.0.0-linux-x86_64.tar.gz
GOOGLE_PROJECT_ID=kf-feast
KUBE_CLUSTER_NAME=primary-test-cluster
KUBE_CLUSTER_ZONE=us-central1-a

curl -s ${GOOGLE_CLOUD_SDK_ARCHIVE_URL} | tar xz -C /
export PATH=/google-cloud-sdk/bin:${PATH}
gcloud -q components install kubectl &> /var/log/kubectl.install.log

if [[ ${KEY_FILE} ]]; then 
    gcloud -q auth activate-service-account --key-file=${KEY_FILE}
    gcloud -q auth configure-docker
    gcloud -q config set project ${GOOGLE_PROJECT_ID}
    gcloud -q container clusters get-credentials ${KUBE_CLUSTER_NAME} --zone ${KUBE_CLUSTER_ZONE} --project ${GOOGLE_PROJECT_ID}
    export GOOGLE_APPLICATION_CREDENTIALS=${KEY_FILE}
fi

# Restore bash option
set +e