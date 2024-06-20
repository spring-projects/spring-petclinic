#!/usr/bin/env bash
echo "$(date +%Y%m%d_%H%M%S) Entering $(basename "$(realpath "${BASH_SOURCE[0]}")") on host $(hostname)"


source scr__test__pipeline_variables.sh
source scr_local_secrets.sh
source scr_scrub_bitbucket_env_vars.sh
source scr_non_secret_env_vars.sh
source scr_validate_mandatory_variables.sh

echo "$(date +%Y%m%d_%H%M%S) Leaving $(basename "$(realpath "${BASH_SOURCE[0]}")") on host $(hostname)"
