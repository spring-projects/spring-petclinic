#!/bin/bash

# Github app uses this script to create check runs
# for a commit sha

/usr/bin/curl \
          -X POST \
          -H "Accept: application/vnd.github+json" \
          -H 'Authorization: Bearer $GITHUB_TOKEN' \
          https://api.github.com/repos/jstan-isch/spring-petclinic/check-runs \
          -d '{"name":"myEvent", "head_sha":"$SHA", "status":"completed", "conclusion":"$STATUS"}'
