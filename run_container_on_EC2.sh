#!/usr/bin/env bash
# -------------------
# This script sets authenthicates and pulls docker image. Then runs it on host

# Global data

IMAGE_NAME="$ECR_NAME:latest"
FULL_KEY_PATH=""

echo "---------------------------------------"
echo ""

read -p "Enter your AWS Key absolute path: " FULL_KEY_PATH

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
DOCKER_LOGIN_CMD=$(aws ecr get-login-password --region "$REGION" | docker login --username AWS --password-stdin "$AWS_ACCOUNT_ID".dkr.ecr."$REGION".amazonaws.com 2>&1)

if [[ $DOCKER_LOGIN_CMD == *"Login Succeeded"* ]]; then
    echo "Authentication with ECR successful."
else
    echo "Error: Failed to authenticate with ECR."
    exit 1
fi

# SSH to EC2 and run instance
echo "SSH-ing to EC2 instance and running Docker image from ECR..."
ssh -i "$FULL_KEY_PATH" ec2-user@"$PUBLIC_IP" <<EOF
# Ensure Docker is installed and running
echo "Updating packages and starting Docker..."
sudo yum update -y
sudo yum install -y docker
sudo service docker start

echo "---------------------------------------"
echo "Authorising ECR..."
# Authorize ECR in Docker using IAM role
aws ecr get-login-password --region "$REGION" | docker login --username AWS --password-stdin "$AWS_ACCOUNT_ID".dkr.ecr."$REGION".amazonaws.com

echo "---------------------------------------"
echo "Pulling Docker image from ECR..."
# Pull the Docker image
docker pull "$AWS_ACCOUNT_ID".dkr.ecr."$REGION".amazonaws.com/"$IMAGE_NAME"

echo "---------------------------------------"
echo "Running spring-petclinic container..."
# Run the Docker image
docker run -d --name spring-pertlinic \
    -p 8080:8080 "$AWS_ACCOUNT_ID".dkr.ecr."$REGION".amazonaws.com/"$IMAGE_NAME"

# Check if the docker run command was successful
if [ \$? -eq 0 ]; then
    echo "Docker container started successfully."
else
    echo "Error: Failed to start Docker container."
    exit 1
fi
EOF

if [ $? -eq 0 ]; then
    echo "Docker image has been successfully deployed on EC2 instance."
else
    echo "Failed to deploy Docker image on EC2 instance."
fi
