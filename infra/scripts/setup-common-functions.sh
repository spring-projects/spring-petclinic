#!/usr/bin/env bash

# Get Feast project repository root and scripts directory
export PROJECT_ROOT_DIR=$(git rev-parse --show-toplevel)
export SCRIPTS_DIR=${PROJECT_ROOT_DIR}/infra/scripts

install_test_tools() {
  apt-get -qq update
  apt-get -y install wget netcat kafkacat build-essential
}

print_banner() {
  echo "
============================================================
$1
============================================================
"
}

wait_for_docker_image(){
  # This script will block until a docker image is ready

  [[ -z "$1" ]] && { echo "Please pass the docker image URI as the first parameter" ; exit 1; }
  oldopt=$-
  set +e

  DOCKER_IMAGE=$1
  poll_count=0
  maximum_poll_count=150

  # Wait for Feast Core to be available on GCR
  until docker pull "$DOCKER_IMAGE"
  do
    # Exit when we have tried enough times
    if [[ "$poll_count" -gt "$maximum_poll_count" ]]; then
         set -$oldopt
         exit 1
    fi
    # Sleep and increment counter on failure
    echo "${DOCKER_IMAGE} could not be found";
    sleep 5;
    ((poll_count++))
  done

  set -$oldopt
}

# Usage: TAG=$(get_tag_release [-ms])
# Parses the last release from git tags.
# Options:
# -m - Use only tags that are tagged on the current branch
# -s - Use only stable version tags. (ie no prerelease tags).
get_tag_release() {
  local GIT_TAG_CMD="git tag -l"
  # Match only Semver tags
  # Regular expression should match MAJOR.MINOR.PATCH[-PRERELEASE[.IDENTIFIER]]
  # eg. v0.7.1 v0.7.2-alpha v0.7.2-rc.1
  local TAG_REGEX='^v[0-9]+\.[0-9]+\.[0-9]+(-([0-9A-Za-z-]+(\.[0-9A-Za-z-]+)*))?$'
  local OPTIND opt
  while getopts "ms" opt; do
    case "${opt}" in
      m)
        GIT_TAG_CMD="$GIT_TAG_CMD --merged"
        ;;
      s)
        # Match only stable version tags.
        TAG_REGEX="^v[0-9]+\.[0-9]+\.[0-9]+$"
        ;;
      *)
        echo "get_tag_release(): Error: Bad arguments: $@"
        return 1
        ;;
    esac
  done
  shift $((OPTIND-1))

  # Retrieve tags from git and filter as per regex.
  local FILTERED_TAGS=$(bash -c "$GIT_TAG_CMD" | grep -P "$TAG_REGEX")

  # Sort version tags in highest semver version first.
  # To make sure that prerelease versions (ie versions vMAJOR.MINOR.PATCH-PRERELEASE suffix)
  # are sorted after stable versions (ie vMAJOR.MINOR.PATCH), we append '_' after 
  # eachustable version as '_' is after '-' found in prerelease version
  # alphanumerically and remove after sorting.
  local SEMVER_SORTED_TAGS=$(echo "$FILTERED_TAGS" | sed -e '/-/!{s/$/_/}' | sort -rV \
    | sed -e 's/_$//')
  echo $(echo "$SEMVER_SORTED_TAGS" | head -n 1)
}
