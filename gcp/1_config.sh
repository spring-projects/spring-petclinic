#!/usr/bin/env bash

PROJECT_ID="${PROJECT_ID:-$(gcloud config get-value project 2>/dev/null)}"


REGION="northamerica-northeast1"
ZONE="${REGION}-a"


PREFIX="ana"

MY_IP=0.0.0.0
NETWORK="${PREFIX}-petclinic-vpc"
SUBNET="${PREFIX}-petclinic-subnet"
SUBNET_RANGE="10.10.0.0/24"
FIREWALL_SSH="${PREFIX}-petclinic-allow-ssh"
FIREWALL_HTTP="${PREFIX}-petclinic-allow-http"
NETWORK_TAG="${PREFIX}-petclinic"
IP_NAME="${PREFIX}-petclinic-ip"
VM_NAME="${PREFIX}-petclinic-vm"
MACHINE_TYPE="e2-medium"
STARTUP_SCRIPT="$(dirname "$0")/vm-startup.sh"

REPO_NAME="${PREFIX}-petclinic-repo"
REGISTRY_HOST="${REGION}-docker.pkg.dev"
IMAGE_NAME="petclinic"
IMAGE_TAG="latest"
REMOTE_IMAGE="${REGISTRY_HOST}/${PROJECT_ID}/${REPO_NAME}/${IMAGE_NAME}:${IMAGE_TAG}"

LOCAL_IMAGE="spring-petclinic-app"

if [[ -z "${PROJECT_ID}" ]]; then
  echo "ERROR: No project set. Run: gcloud config set project YOUR_PROJECT_ID" >&2
  exit 1
fi

echo "Project : ${PROJECT_ID}"
echo "Region  : ${REGION}  Zone: ${ZONE}"
echo "Image   : ${REMOTE_IMAGE}"
echo