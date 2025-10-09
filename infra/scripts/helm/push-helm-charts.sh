#!/usr/bin/env bash

set -e

if [ $# -ne 1 ]; then
    echo "Please provide a single semver version (without a \"v\" prefix) to test the repository against, e.g 0.99.0"
    exit 1
fi

bucket=gs://feast-helm-charts
repo_url=https://feast-helm-charts.storage.googleapis.com/

helm plugin install https://github.com/hayorov/helm-gcs.git  --version 0.3.18  || true

helm repo add feast-helm-chart-repo $bucket

cd infra/charts
helm package feast
helm package feast-feature-server

helm gcs push --public feast-${1}.tgz feast-helm-chart-repo --force
helm gcs push --public feast-feature-server-${1}.tgz feast-helm-chart-repo --force
rm -f ./*.tgz