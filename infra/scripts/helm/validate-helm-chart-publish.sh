#!/usr/bin/env bash

# Get project root
PROJECT_ROOT_DIR=$(git rev-parse --show-toplevel)

# Should have no "latest" tags
grep -R "tag: latest" "$PROJECT_ROOT_DIR"/infra/charts || true
COUNT=$(grep -R "tag: latest" "$PROJECT_ROOT_DIR"/infra/charts | wc -l)

if [ "$COUNT" -gt 0 ]; then
  echo 'Found more than one instance of "latest" in an image tag. Please replace with correct release version.';
  exit 1
else
  echo 'No "latest" tags found, continuing';
fi

# TODO: Enable DockerHub vs GCR checks asap.

## Should have no "gcr" images
#grep -R "gcr.io" "$PROJECT_ROOT_DIR"/infra/charts || true
#COUNT=$(grep -R "gcr.io" "$PROJECT_ROOT_DIR"/infra/charts | wc -l)
#
#if [ "$COUNT" -gt 0 ]; then
#  echo 'Found more than one instance of "gcr.io" in charts. Please replace with https://hub.docker.com/r/feastdev feast image.';
#  exit 1
#else
#  echo 'No "gcr.io" instances found, continuing';
#fi

# Should have no "SNAPSHOT" versions
grep -R "SNAPSHOT" "$PROJECT_ROOT_DIR"/infra/charts || true
COUNT=$(grep -R "SNAPSHOT" "$PROJECT_ROOT_DIR"/infra/charts | wc -l)

if [ "$COUNT" -gt 0 ]; then
  echo 'Found more than one instance of "SNAPSHOT" in charts. Please ensure that no SNAPSHOT charts are published.';
  exit 1
else
  echo 'No "SNAPSHOT" instances found, continuing';
fi