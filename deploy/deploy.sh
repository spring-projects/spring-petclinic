#!/usr/bin/env bash

set -v

VAULT_FILE="/tmp/vault"
DEPLOYMENT_ENVIRONMENT=${$1:=develop}

# Only execute deployment if $TRAVIS is null (we are not on CI) or we are on CI merging to master and the build has ended successfully
if [[ -z "${TRAVIS}" ]] || [[ "${TRAVIS_PULL_REQUEST}" == "false" && "${TRAVIS_TEST_RESULT}" == 0 && "${TRAVIS_BRANCH}" == 'master' ]]; then
  echo "${VAULT_PASSWORD}" > "${VAULT_FILE}"
  virtualenv .
  . bin/activate
  pip install -r deploy/requirements.txt
  printf '[defaults]\nroles_path=../' >ansible.cfg
  ansible-galaxy install -p ../ -r deploy/requirements.yml
  ansible-playbook deploy/deploy.yml -vvvv --vault-password-file="${VAULT_FILE}" --extra-vars "wimpy_release_version=${TRAVIS_COMMIT} wimpy_deployment_environment=${DEPLOYMENT_ENVIRONMENT}"
fi
