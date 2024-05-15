#!/usr/bin/env bash
# -------------------
# This script sets authenthicates and pulls docker image. Then runs it on host

# Global data
REGION="eu-west-1"
IMAGE_NAME=""

# Get data from user
read -p "Enter the ECR image name: " IMAGE_NAME

# Get the public IP of EC2 instance from previus script
echo "Getting public IP address of EC2 instance..."
PUBLIC_IP=$(aws ec2 describe-instances --instance-ids "$INSTANCE_ID" --query 'Reservations[0].Instances[0].PublicIpAddress' --output text --region "$REGION")

if [ -z "$PUBLIC_IP" ]; then
    echo "Error: Failed to get public IP address of EC2 instance."
    exit 1
fi
echo "Public IP address of EC2 instance: $PUBLIC_IP"

# Authorize ECR in docker
echo "Getting authentication token for ECR..."
DOCKER_LOGIN_CMD=$(aws ecr get-login-password --region "$REGION" | docker login --username AWS --password-stdin "$ACCOUNT_ID".dkr.ecr."$REGION".amazonaws.com 2>&1)

if [[ $DOCKER_LOGIN_CMD == *"Login Succeeded"* ]]; then
    echo "Authentication with ECR successful."
else
    echo "Error: Failed to authenticate with ECR."
    exit 1
fi

# SSH to EC2 and run instance
echo "SSH-ing to EC2 instance and running Docker image from ECR..."
ssh -i /path/to/your/private-key.pem ec2-user@"$PUBLIC_IP" <<EOF
docker pull "$IMAGE_NAME"
docker run -d -p 80:8080 "$IMAGE_NAME"
EOF

if [ $? -eq 1 ]; then
    echo "Could not ssh and run docker image from ECR"
fi
echo "Docker image has been successfully deployed on EC2 instance."
