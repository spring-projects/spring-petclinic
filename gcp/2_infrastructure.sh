#!/usr/bin/env bash

set -euo pipefail
source "$(dirname "$0")/1_config.sh"


echo "[1/6] VPC: ${NETWORK}"
if gcloud compute networks describe "${NETWORK}" &>/dev/null; then
  echo "    already exists, skipping"
else
  gcloud compute networks create "${NETWORK}" \
    --subnet-mode=custom \
    --description="VPC for petclinic internship task"
fi


echo " [2/6] Subnet: ${SUBNET} (${SUBNET_RANGE})"
if gcloud compute networks subnets describe "${SUBNET}" --region="${REGION}" &>/dev/null; then
  echo "    already exists, skipping"
else
  gcloud compute networks subnets create "${SUBNET}" \
    --network="${NETWORK}" \
    --region="${REGION}" \
    --range="${SUBNET_RANGE}"
fi


echo "[3a/6] Firewall (SSH): ${FIREWALL_SSH}"
if gcloud compute firewall-rules describe "${FIREWALL_SSH}" &>/dev/null; then
  echo "    exists — updating source ranges to ${MY_IP}"
  gcloud compute firewall-rules update "${FIREWALL_SSH}" --source-ranges="${MY_IP}"
else
  gcloud compute firewall-rules create "${FIREWALL_SSH}" \
    --network="${NETWORK}" \
    --direction=INGRESS \
    --action=ALLOW \
    --rules=tcp:22 \
    --source-ranges="${MY_IP}/0" \
    --target-tags="${NETWORK_TAG}" \
    --description="Allow SSH from admin only"
fi

echo " [3b/6] Firewall (HTTP): ${FIREWALL_HTTP}"
if gcloud compute firewall-rules describe "${FIREWALL_HTTP}" &>/dev/null; then
  echo "    already exists, skipping"
else
  gcloud compute firewall-rules create "${FIREWALL_HTTP}" \
    --network="${NETWORK}" \
    --direction=INGRESS \
    --action=ALLOW \
    --rules=tcp:8080 \
    --source-ranges=0.0.0.0/0 \
    --target-tags="${NETWORK_TAG}" \
    --description="Allow petclinic HTTP from anywhere"
fi


echo " [4/6] Static IP: ${IP_NAME}"
if gcloud compute addresses describe "${IP_NAME}" --region="${REGION}" &>/dev/null; then
  echo "    already exists, skipping"
else
  gcloud compute addresses create "${IP_NAME}" --region="${REGION}"
fi

STATIC_IP=$(gcloud compute addresses describe "${IP_NAME}" \
  --region="${REGION}" --format='value(address)')
echo "    reserved address: ${STATIC_IP}"


echo " [5/6] Artifact Registry repo: ${REPO_NAME}"
if gcloud artifacts repositories describe "${REPO_NAME}" --location="${REGION}" &>/dev/null; then
  echo "    already exists, skipping"
else
  gcloud artifacts repositories create "${REPO_NAME}" \
    --repository-format=docker \
    --location="${REGION}" \
    --description="Private docker repo for petclinic"
fi


echo " [6/6] VM: ${VM_NAME}"
if gcloud compute instances describe "${VM_NAME}" --zone="${ZONE}" &>/dev/null; then
  echo "    already exists, skipping"
else
  gcloud compute instances create "${VM_NAME}" \
    --zone="${ZONE}" \
    --machine-type="${MACHINE_TYPE}" \
    --subnet="${SUBNET}" \
    --address="${STATIC_IP}" \
    --image-family=debian-12 \
    --image-project=debian-cloud \
    --boot-disk-size=20GB \
    --tags="${NETWORK_TAG}" \
    --scopes=cloud-platform
fi

echo
echo " Infrastructure ready."
echo " App will be at : http://${STATIC_IP}:8080  (after 03-deploy)"
