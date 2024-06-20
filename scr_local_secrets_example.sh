#!/usr/bin/env bash
echo "$(date +%Y%m%d_%H%M%S) - Entering $(basename "$(realpath "${BASH_SOURCE[0]}")") on host $(hostname)"

export HARBOR_USERNAME="barry_matheney@yahoo.com"
export HARBOR_LOGIN_ADDRESS="c8n.io/barrymatheney"
export HARBOR_ACCESS_TOKEN="whatever the access token that you created is"

echo "$(date +%Y%m%d_%H%M%S) - Leaving $(basename "$(realpath "${BASH_SOURCE[0]}")") on host $(hostname)"

