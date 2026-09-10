#!/usr/bin/env bash

set -euo pipefail
cd "$(dirname "$0")"

START=$(date +%s)

banner() {
  echo
  echo "# $1"
  echo
}

banner "STEP 1/4  Create infrastructure (VPC, subnet, firewall, IP, registry, VM)"
./2_infrastructure.sh

banner "STEP 2/4  Push image to private Artifact Registry"
./3_push_image.sh

banner "STEP 3/4  Install Docker on VM and run the container"
./4_deploy.sh

banner "STEP 4/4  Verify every requirement"
./5_verify.sh

END=$(date +%s)
echo
echo "# Done in $(( (END - START) / 60 ))m $(( (END - START) % 60 ))s"
