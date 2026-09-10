#!/usr/bin/env bash
# Deletes everything created by 01 and 02, in reverse dependency order.
# Run this when you are done — a reserved static IP and a running VM both cost money.

set -uo pipefail   # deliberately NOT -e: keep deleting even if one step fails
source "$(dirname "$0")/1_config.sh"

echo "This will DELETE the following in project ${PROJECT_ID}:"
echo "  VM              ${VM_NAME}"
echo "  Static IP       ${IP_NAME}"
echo "  Firewalls        "
echo "  Subnet          ${SUBNET}"
echo "  VPC             ${NETWORK}"
echo "  Registry repo   ${REPO_NAME}  (including the pushed image)"
echo
read -r -p "Type 'yes' to continue: " CONFIRM
[[ "${CONFIRM}" == "yes" ]] || { echo "Aborted."; exit 1; }

# Deletion order is the exact reverse of creation. You cannot delete a subnet
# while a VM sits in it, or a VPC while a subnet is inside it.

echo " [1/6] Deleting VM"
gcloud compute instances delete "${VM_NAME}" --zone="${ZONE}" --quiet || echo "    (not found)"

echo "2/6] Releasing static IP"
gcloud compute addresses delete "${IP_NAME}" --region="${REGION}" --quiet || echo "    (not found)"

echo "[3/6] Deleting firewall rule"
gcloud compute firewall-rules delete "${FIREWALL_SSH}" --quiet || echo "    (not found)"
gcloud compute firewall-rules delete "${FIREWALL_HTTP}" --quiet || echo "    (not found)"

echo "4/6] Deleting subnet"
gcloud compute networks subnets delete "${SUBNET}" --region="${REGION}" --quiet || echo "    (not found)"

echo "[5/6] Deleting VPC"
gcloud compute networks delete "${NETWORK}" --quiet || echo "    (not found)"

echo "[6/6] Deleting Artifact Registry repo"
gcloud artifacts repositories delete "${REPO_NAME}" --location="${REGION}" --quiet || echo "    (not found)"

echo
echo " Verifying nothing is left"
echo "-- instances --"; gcloud compute instances list      --filter="name=${VM_NAME}"  2>/dev/null
echo "-- addresses --"; gcloud compute addresses list      --filter="name=${IP_NAME}"  2>/dev/null
echo "-- subnets   --"; gcloud compute networks subnets list --filter="name=${SUBNET}" 2>/dev/null
echo "-- networks  --"; gcloud compute networks list       --filter="name=${NETWORK}"  2>/dev/null
echo "-- repos     --"; gcloud artifacts repositories list --location="${REGION}" --filter="name~${REPO_NAME}" 2>/dev/null
echo
echo "Empty output above = clean."