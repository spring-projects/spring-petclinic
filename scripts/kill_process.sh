#!/bin/bash

echo "Remove existed container"
docker-compose -f /home/ubuntu/docker-compose.yml down || true

