#!/bin/sh

# Github app uses this script to create check runs
# for a commit sha

echo CONTEXT="$CONTEXT"
echo SHA="$SHA"
echo STATUS="$STATUS"
echo GITHUB_TOKEN="$GITHUB_TOKEN"

curl \
  -X POST \
  -H "Accept: application/vnd.github+json" \
  -H "Authorization: Bearer ${GITHUB_TOKEN}" \
  https://api.github.com/repos/jstan-isch/spring-petclinic/check-runs \
 -d '{"name":"${CONTEXT}", "head_sha":"${SHA}", "status":"completed", "conclusion":"${STATUS}"}'
