#!/usr/bin/env bash
# -------------------
# This script sets builds and pushes image to previously made ECR
# 
# Note: docker is reqiured

# Global data
source ./prepare_aws_enviroment.sh

AWS_ACCOUNT_ID=""

# Get data from user
read -p "Enter your AWS account ID: " AWS_ACCOUNT_ID

# Build docker image locally
echo "Building Docker image..."
if docker build -t spring-petclinic .; then
    echo "Docker image built successfully."
else
    echo "Error: Failed to build Docker image."
    exit 1
fi

# Log in to ECR
echo "Logging in to Amazon ECR..."
DOCKER_LOGIN_CMD=$(aws ecr get-login-password --region "$REGION")

if [ $? -eq 0 ]; then
    echo "Got credentials from AWS CLI."
else
    echo "Error: Failed to get credentials from AWS CLI."
    exit 1
fi

if echo "$DOCKER_LOGIN_CMD" | docker login --username AWS --password-stdin "$AWS_ACCOUNT_ID".dkr.ecr."$REGION".amazonaws.com; then
    echo "Logged in to ECR successfully."
else
    echo "Error: Failed to log in to ECR."
    exit 1
fi

# Tag the image
echo "Tagging Docker image..."
if docker tag spring-petclinic:latest "$AWS_ACCOUNT_ID".dkr.ecr."$REGION".amazonaws.com/"$ECR_NAME":latest; then
    echo "Docker image tagged successfully."
else
    echo "Error: Failed to tag Docker image."
    exit 1
fi

# Push image to ECR
echo "Pushing Docker image to ECR..."
if docker push "$AWS_ACCOUNT_ID".dkr.ecr."$REGION".amazonaws.com/"$ECR_NAME":latest; then
    echo "Docker image pushed to ECR successfully."
else
    echo "Error: Failed to push Docker image to ECR."
    exit 1
fi

echo "Docker image has been successfully pushed to ECR."

./run_container_on_EC2.sh