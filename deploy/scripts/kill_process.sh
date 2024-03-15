#!/bin/bash

echo "Remove existed container"
cd /home/ubuntu/deploy
docker-compose -f deploy/docker-compose.yml down || true
