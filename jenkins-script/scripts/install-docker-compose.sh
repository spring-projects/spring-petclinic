#!/bin/bash
## INFO: https://docs.docker.com/compose/install/

set -euf pipefail
DOCKER_COMPOSE_VERSION=v2.1.1

# Download and install
curl -L "https://github.com/docker/compose/releases/download/${DOCKER_COMPOSE_VERSION}/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose