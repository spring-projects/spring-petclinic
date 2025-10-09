#!/usr/bin/env bash
set -e
readonly HELM_URL=https://storage.googleapis.com/kubernetes-helm
readonly HELM_TARBALL="helm-${HELM_VERSION}-linux-amd64.tar.gz"
readonly STABLE_REPO_URL=https://charts.helm.sh/stable
readonly INCUBATOR_REPO_URL=https://charts.helm.sh/incubator
curl -s "https://get.helm.sh/helm-${HELM_VERSION}-linux-amd64.tar.gz" | tar -C /tmp -xz
sudo mv /tmp/linux-amd64/helm /usr/bin/helm
helm repo add incubator "$INCUBATOR_REPO_URL"
