#!/bin/bash


set -e
export DEBIAN_FRONTEND=noninteractive

echo "==> Installing Docker"
apt-get update -qq
apt-get install -y -qq docker.io
systemctl enable --now docker

echo "Configuring Artifact Registry auth"
gcloud auth configure-docker __REGISTRY_HOST__ --quiet

echo " Pulling image (with retry)"
for i in {1..20}; do
  if docker pull __REMOTE_IMAGE__; then break; fi
  echo "    attempt $i/20 — image not ready, waiting 15s"
  sleep 15
done

echo "Running container"
docker rm -f petclinic 2>/dev/null || true
docker run -d \
  --name petclinic \
  --restart unless-stopped \
  -p 8080:8080 \
  __REMOTE_IMAGE__

echo "Startup complete"