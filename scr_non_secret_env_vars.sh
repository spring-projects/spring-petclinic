#!/usr/bin/env bash
echo "$(date +%Y%m%d_%H%M%S) - Entering $(basename "$(realpath "${BASH_SOURCE[0]}")") on host $(hostname)"

echo "$(date +%Y%m%d_%H%M%S) - Leaving $(basename "$(realpath "${BASH_SOURCE[0]}")") on host $(hostname)"

