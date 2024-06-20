#!/usr/bin/env bash
echo "$(date +%Y%m%d_%H%M%S) - Entering $(basename "$(realpath "${BASH_SOURCE[0]}")") on host $(hostname)"

export DOCKER_IMAGE_NAME="${HARBOR_LOGIN_ADDRESS}/${BITBUCKET_REPO_FULL_NAME_SCRUBBED}"
export DOCKER_TAG_PIPELINE_UUID="${BITBUCKET_PIPELINE_UUID_SCRUBBED}"
export DOCKER_TAG_COMMIT_SHA="${BITBUCKET_COMMIT_SCRUBBED}"
export DOCKER_TAG_BRANCH_NAME="${BITBUCKET_BRANCH_SCRUBBED}"
export DOCKER_TAG_BRANCH_NAME="${BITBUCKET_PIPELINE_UUID_SCRUBBED}"
echo "$(date +%Y%m%d_%H%M%S) - Leaving $(basename "$(realpath "${BASH_SOURCE[0]}")") on host $(hostname)"

