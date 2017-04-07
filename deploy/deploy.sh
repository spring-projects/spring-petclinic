#!/usr/bin/env bash

virtualenv .
. bin/activate
pip install -r deploy/requirements.txt
printf '[defaults]\nroles_path=../' >ansible.cfg
ansible-galaxy install -p ../ -r deploy/requirements.yml
ansible-playbook deploy/deploy.yml -vvvv --vault-password-file=/tmp/vault --extra-vars "wimpy_release_version=${TRAVIS_COMMIT} wimpy_deployment_environment=develop"
