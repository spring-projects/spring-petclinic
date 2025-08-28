#!/bin/bash

# Check if docker is running
docker ps &> /dev/null
if [ $? -ne 0 ]; then
    echo "Docker Engine not running"
    exit 1
fi

# 0. Clean Up
echo "--------------------------------------------------"
echo "                     CLEANUP                      "
echo "--------------------------------------------------"
chmod u+x cleanup.sh
./cleanup.sh

# 1. Create network
echo "--------------------------------------------------"
echo "                     NETWORK                      "
echo "--------------------------------------------------"
docker network create jenkins

# 2. Build Jenkins image
echo "--------------------------------------------------"
echo "                  JENKINS IMAGE                   "
echo "--------------------------------------------------"
docker build -f Dockerfile-Jenkins -t myjenkins .

# 3. Build Jenkins Agent image
echo "--------------------------------------------------"
echo "                   AGENT IMAGE                    "
echo "--------------------------------------------------"
docker build -f Dockerfile-Agent -t myjenkins-agent-dind .

# 4. Docker Compose (detached)
echo "--------------------------------------------------"
echo "                       RUN                        "
echo "--------------------------------------------------"
docker-compose up -d