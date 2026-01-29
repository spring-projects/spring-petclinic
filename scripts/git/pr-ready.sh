#!/usr/bin/env bash
set -e

echo "=== PR Readiness Check ==="

CURRENT_BRANCH=$(git branch --show-current)

if [[ "$CURRENT_BRANCH" == "develop" || "$CURRENT_BRANCH" == "main" ]]; then
  echo "❌ PR cannot be raised from $CURRENT_BRANCH"
  exit 1
fi

echo "✔ Current branch: $CURRENT_BRANCH"

git fetch origin

if git merge-base --is-ancestor origin/develop HEAD; then
  echo "✔ Branch is up to date with develop"
else
  echo "❌ Branch is behind develop"
  exit 1
fi

echo "✔ Running tests (POC)"
./mvnw test || { echo "❌ Tests failed"; exit 1; }

echo "✅ PR is READY"
