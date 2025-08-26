#!/bin/bash

# Check if docker is running
docker ps &> /dev/null
if [ $? -ne 0 ]; then
    echo "Docker Engine not running"
    exit 1
fi

# Stop containers
if [ -z "$(docker ps | grep "[ ]+jenkins$")" ]; then
    echo "--------------------------------------------------"
    echo "Stopping jenkins container..."
    docker stop jenkins 2> /dev/null
    if [ $? != 0 ]; then
        echo "Couldn't stop jenkins container."
    else
        echo "Done."
    fi
fi

if [ -z "$(docker ps | grep "[ ]+agent$")" ]; then
    echo "--------------------------------------------------"
    echo "Stopping agent container..."
    docker stop agent 2> /dev/null
    if [ $? != 0 ]; then
        echo "Couldn't stop agent container."
    else
        echo "Done."
    fi
fi

# Remove containers
if [ -z "$(docker ps -a | grep "[ ]+jenkins$")" ]; then
    echo "--------------------------------------------------"
    echo "Removing jenkins container..."
    docker rm jenkins 2> /dev/null
    if [ $? != 0 ]; then
        echo "Couldn't remove jenkins container."
    else
        echo "Done."
    fi
fi

if [ -z "$(docker ps -a | grep "[ ]+agent$")" ]; then
    echo "--------------------------------------------------"
    echo "Removing agent container..."
    docker rm agent 2> /dev/null
    if [ $? != 0 ]; then
        echo "Couldn't remove agent container."
    else
        echo "Done."
    fi
fi

# Remove network "jenkins"
if [ -z "$(docker network ls | grep "[ ]+jenkins[ ]+")" ]; then
    echo "--------------------------------------------------"
    echo "Removing jenkins network..."
    docker network rm jenkins 2> /dev/null
    if [ $? != 0 ]; then
        echo "Couldn't remove jenkins network."
    else
        echo "Done."
    fi
fi

# Remove images
if [ -z "$(docker images | grep "^myjenkins[ ]+")" ]; then
    echo "--------------------------------------------------"
    echo "Removing myjenkins image..."
    docker rmi myjenkins:latest 2> /dev/null
    if [ $? != 0 ]; then
        echo "Couldn't remove myjenkins image."
    else
        echo "Done."
    fi
fi

if [ -z "$(docker images | grep "^myjenkins-agent-dind[ ]+")" ]; then
    echo "--------------------------------------------------"
    echo "Removing myjenkins-agent-dind image..."
    docker rmi myjenkins-agent-dind:latest 2> /dev/null
    if [ $? != 0 ]; then
        echo "Couldn't remove myjenkins-agent-dind image."
    else
        echo "Done."
    fi
fi

echo "--------------------------------------------------"