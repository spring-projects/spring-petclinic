#!/usr/bin/env bash

set -euo pipefail
source "$(dirname "$0")/1_config.sh"

STATIC_IP=$(gcloud compute addresses describe "${IP_NAME}" \
  --region="${REGION}" --format='value(address)')

gcloud compute networks list --filter="name=${NETWORK}"
gcloud compute networks subnets list --filter="name=${SUBNET}"

echo
gcloud compute addresses describe "${IP_NAME}" --region="${REGION}" \
  --format="table(name,address,status,users)"

echo
gcloud artifacts docker images list \
  "${REGISTRY_HOST}/${PROJECT_ID}/${REPO_NAME}" --include-tags

echo
if gcloud artifacts repositories get-iam-policy "${REPO_NAME}" \
     --location="${REGION}" --format=json | grep -qE 'allUsers|allAuthenticatedUsers'; then
  echo "PUBLIC — unexpected!"
else
  echo "PRIVATE — no allUsers/allAuthenticatedUsers binding."
fi

echo
gcloud compute instances list --filter="name=${VM_NAME}" \
  --format="table(name,zone,machineType.basename(),status,networkInterfaces[0].accessConfigs[0].natIP)"

echo
gcloud compute ssh "${VM_NAME}" --zone="${ZONE}" --quiet \
  --command="sudo docker ps --format 'table {{.Names}}\t{{.Image}}\t{{.Status}}\t{{.Ports}}'"

echo
echo "Polling http://${STATIC_IP}:8080 (Spring Boot needs ~30-60s to warm up)"
for i in {1..24}; do
  CODE=$(curl -s -o /dev/null -w '%{http_code}' --max-time 5 "http://${STATIC_IP}:8080" || echo "000")
  if [[ "${CODE}" =~ ^(200|302)$ ]]; then
    echo "SUCCESS: HTTP ${CODE} from http://${STATIC_IP}:8080"
    exit 0
  fi
  echo "  attempt ${i}/24 — got '${CODE}', retrying in 5s"
  sleep 5
done

echo "FAILED: app did not respond in time."
echo "Debug with:"
echo "  gcloud compute ssh ${VM_NAME} --zone=${ZONE} --command='sudo docker logs petclinic'"
exit 1