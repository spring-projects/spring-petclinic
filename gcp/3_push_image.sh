#!/usr/bin/env bash

set -euo pipefail
source "$(dirname "$0")/1_config.sh"

if ! docker image inspect "${LOCAL_IMAGE}" &>/dev/null; then
  echo "ERROR: local image '${LOCAL_IMAGE}' not found." >&2
  echo "Build it first, from your spring-petclinic checkout:" >&2
  echo "    docker build -t ${LOCAL_IMAGE} ." >&2
  exit 1
fi

echo "Configuring docker credential helper for ${REGISTRY_HOST}"
gcloud auth configure-docker "${REGISTRY_HOST}" --quiet


echo "Tagging ${LOCAL_IMAGE} -> ${REMOTE_IMAGE}"
docker tag "${LOCAL_IMAGE}" "${REMOTE_IMAGE}"


echo "Pushing"
docker push "${REMOTE_IMAGE}"


echo
echo "Images now in ${REPO_NAME}:"
gcloud artifacts docker images list \
  "${REGISTRY_HOST}/${PROJECT_ID}/${REPO_NAME}" \
  --include-tags


echo
echo "Checking repository is private"
POLICY=$(gcloud artifacts repositories get-iam-policy "${REPO_NAME}" \
  --location="${REGION}" --format=json)

if echo "${POLICY}" | grep -qE 'allUsers|allAuthenticatedUsers'; then
  echo "    WARNING: repository is PUBLIC — public members found in IAM policy:"
  echo "${POLICY}"
  exit 1
else
  echo "    OK: no allUsers/allAuthenticatedUsers binding. Repository is private."
fi