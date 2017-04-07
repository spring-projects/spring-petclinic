#!/usr/bin/env bash

set -eu

virtualenv .
source "bin/activate"
pip install -r requirements.txt
printf '[defaults]\nroles_path=../' >ansible.cfg
ansible-galaxy install -p ../ -r deploy/requirements.yml
