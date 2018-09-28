#!/usr/bin/env bash

########################################################
#
# Name: create-k8s-node-pool.sh
#
##########################################################

POOL_NAME='cicd-k8s-pool'
CLUSTER_NAME='cicd'
CLUSTER_ZONE='europe-west1-b'
SCOPES='compute-rw,storage-rw,logging-write,monitoring-write'

gcloud container node-pools create "${POOL_NAME}" \
        --cluster="${CLUSTER_NAME}" \
        --disk-size='15' \
        --disk-type='pd-standard' \
        --enable-autorepair \
        --enable-autoupgrade \
        --image-type='COS' \
        --machine-type='custom-1-3072' \
        --min-cpu-platform='Automatic' \
        --node-version='1.10.7-gke.2' \
        --num-nodes='3' \
        --zone="${CLUSTER_ZONE}" \
        --scopes="${SCOPES}"
