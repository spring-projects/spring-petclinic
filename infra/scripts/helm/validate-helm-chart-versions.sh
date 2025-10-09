#!/usr/bin/env bash

set -e

# Amount of file locations that need to be bumped in unison when versions increment
UNIQUE_VERSIONS_COUNT=20 # Change in release 0.27.0

if [ $# -ne 1 ]; then
    echo "Please provide a single semver version (without a \"v\" prefix) to test the repository against, e.g 0.99.0"
    exit 1
fi

CHART_ROOT_DIR=$(git rev-parse --show-toplevel)/infra/charts


echo "Finding how many versions have been set to ${1} in the current repository"

CHANGED_VERSIONS_COUNT=$(grep -R --exclude-dir='.*' ${1} ${CHART_ROOT_DIR} | wc -l)
echo "Found ${CHANGED_VERSIONS_COUNT} versions that have been changed"

echo "This repository should contain ${UNIQUE_VERSIONS_COUNT} changed versions"

if [ $UNIQUE_VERSIONS_COUNT -ne "${CHANGED_VERSIONS_COUNT}" ]; then
    echo "We expected $UNIQUE_VERSIONS_COUNT to have been updated to the latest version, but only ${CHANGED_VERSIONS_COUNT} have. This number is statically defined based on a simple grep"
    echo "Please confirm that all versions in all charts and requirements files have been bumped to the tagged release version. If you have successfully bumped all versions and there is still a mismatch in the expected and actual counts, then rerun the following command"
    echo "grep -R 'insert_your_semver_version_here' . | wc -l"
    echo "and update the script scripts/validate-helm-chart-versions.sh"
    echo
    echo For your reference, the following lines were detected as changed
    echo
    grep -R --exclude-dir='.*' ${1} ${CHART_ROOT_DIR} || true
    echo
    exit 1
fi

echo "All versions validated. Passing test."
