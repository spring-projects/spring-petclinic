#!/bin/sh

# Github app uses this script to create check runs
# for a commit sha

echo NAME="myEvent"
echo STATUS="success"
echo SHA="$SHA"
echo GITHUB_TOKEN="$GITHUB_TOKEN"

/usr/bin/curl \
          -X POST \
          -H "Accept: application/vnd.github+json" \
          -H "Authorization: Bearer $GITHUB_TOKEN" \
          https://api.github.com/repos/jstan-isch/spring-petclinic/check-runs \
          -d '{"name":"$NAME", "head_sha":"$SHA", "status":"completed", "conclusion":"success"}'
