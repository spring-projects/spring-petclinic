#!/usr/bin/env bash

set -euo pipefail
source "$(dirname "$0")/1_config.sh"

STATIC_IP=$(gcloud compute addresses describe "${IP_NAME}" \
  --region="${REGION}" --format='value(address)')

PROJECT_NUMBER=$(gcloud projects describe "${PROJECT_ID}" --format='value(projectNumber)')
COMPUTE_SA="${PROJECT_NUMBER}-compute@developer.gserviceaccount.com"

echo "Granting artifactregistry.reader to ${COMPUTE_SA}"
if gcloud projects add-iam-policy-binding "${PROJECT_ID}" \
     --member="serviceAccount:${COMPUTE_SA}" \
     --role="roles/artifactregistry.reader" \
     --condition=None >/dev/null 2>&1; then
  echo "    granted (or already present)"
else
  echo "    WARNING: could not modify IAM (likely insufficient permissions)."
  echo "    Continuing — the pull may still work if the role is already granted."
fi


echo " Waiting for SSH on ${VM_NAME}"
for i in {1..20}; do
  if gcloud compute ssh "${VM_NAME}" --zone="${ZONE}" \
       --command="echo ready" --quiet &>/dev/null; then
    echo "    SSH is up"
    break
  fi
  echo "    attempt ${i}/20 — not ready yet, sleeping 10s"
  sleep 10
done

echo "Installing Docker on the VM"
gcloud compute ssh "${VM_NAME}" --zone="${ZONE}" --quiet --command="
  set -e
  export DEBIAN_FRONTEND=noninteractive
  sudo apt-get update -qq
  sudo apt-get install -y -qq docker.io
  sudo systemctl enable --now docker
  sudo docker --version
"


# ---------------------------------------------------------------------------
echo "Configuring registry auth on the VM"
gcloud compute ssh "${VM_NAME}" --zone="${ZONE}" --quiet --command="
  sudo gcloud auth configure-docker ${REGISTRY_HOST} --quiet
"

echo "Pulling and running the container"
gcloud compute ssh "${VM_NAME}" --zone="${ZONE}" --quiet --command="
  set -e
  sudo docker rm -f petclinic 2>/dev/null || true
  sudo docker pull ${REMOTE_IMAGE}
  sudo docker run -d \
    --name petclinic \
    --restart unless-stopped \
    -p 8080:8080 \
    ${REMOTE_IMAGE}
  sudo docker ps
"

echo
echo " Deployed."
echo " Open: http://${STATIC_IP}:8080"
