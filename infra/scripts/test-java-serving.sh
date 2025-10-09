#!/usr/bin/env bash

mvn -f java/pom.xml --batch-mode --also-make --projects serving test
TEST_EXIT_CODE=$?

# Default artifact location setting in Prow jobs
LOGS_ARTIFACT_PATH=/logs/artifacts
cp -r serving/target/surefire-reports ${LOGS_ARTIFACT_PATH}/surefire-reports

exit ${TEST_EXIT_CODE}
