#!/bin/bash

echo "Remove existed container"
docker-compose -f /home/ec2-user/docker-compose.yml down || true

