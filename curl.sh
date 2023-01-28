#!/bin/bash

# Github app uses this script to create check runs
# for a commit sha

echo PR_CONTEXT="$CONTEXT"
echo COMMIT_SHA="$SHA"
echo PR_STATUS="$STATUS"
echo TOKEN="$GITHUB_TOKEN"

PR_CONTEXT="$CONTEXT"
COMMIT_SHA="$SHA"
PR_STATUS="$STATUS"
TOKEN="$GITHUB_TOKEN"

curl \
  -X POST \
  -H "Accept: application/vnd.github+json" \
  -H "Authorization: Bearer ${TOKEN}" \
  https://api.github.com/repos/jstan-isch/spring-petclinic/check-runs \
 -d '{"name":"${PR_CONTEXT}", "head_sha":"${COMMIT_SHA}", "status":"completed", "conclusion":"${PR_STATUS}"}'
