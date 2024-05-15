#!/usr/bin/env bash
# -------------------
# This script sets up basic AWS environment for pushing docker images into the cloud
# It creates: VPC, Subnet, Elastic Container Registry (ECR), EC2 instance with a public IP, Security Groups
#
# Note: script needs preconfigured AWS CLI

# Global data
REGION="eu-west-1"
VPC_ID=""
SUBNET_ID=""
SECURITY_GROUP_ID=""
ECR_REPO_URI=""
INSTANCE_ID=""

# Get data from user - set it as env to be used in later scripts
read -p "Enter VPC name: " VPC_NAME && export VPC_NAME
read -p "Enter owner name: " OWNER && export OWNER
read -p "Enter project name: " PROJECT && export PROJECT
read -p "Enter ECR repository name: " ECR_NAME && export ECR_NAME
read -p "Enter EC2 instance name: " INSTANCE_NAME && export INSTANCE_NAME
read -p "Enter security group name: " SECURITY_GROUP_NAME && export SECURITY_GROUP_NAME
read -p "Enter key pair name: " KEY_PAIR_NAME && export KEY_PAIR_NAME

# Create VPC
echo "Creating VPC..."
VPC_ID=$(aws ec2 create-vpc --cidr-block 10.0.0.0/16 --region "$REGION" --query 'Vpc.VpcId' --output text)

if [ -z "$VPC_ID" ]; then
    echo "Error during VPC creation."
    exit 1
fi
echo "VPC with ID $VPC_ID has been created."

# Add tags to VPC
aws ec2 create-tags --resources "$VPC_ID" --tags Key=Name,Value="$VPC_NAME" Key=Owner,Value="$OWNER" Key=Project,Value="$PROJECT" --region "$REGION"
echo "VPC is now correctly configured."

# Create Subnet
echo "Creating Subnet..."
SUBNET_ID=$(aws ec2 create-subnet --vpc-id "$VPC_ID" --cidr-block 10.0.0.0/24 --availability-zone "$REGION"a --query 'Subnet.SubnetId' --output text)

if [ -z "$SUBNET_ID" ]; then
    echo "Error during Subnet creation."
    exit 1
fi
echo "Subnet with ID $SUBNET_ID has been created."

# Add tags to Subnet
aws ec2 create-tags --resources "$SUBNET_ID" --tags Key=Name,Value="$VPC_NAME-Subnet" Key=Owner,Value="$OWNER" Key=Project,Value="$PROJECT" --region "$REGION"
echo "Subnet is now correctly configured."

# Create Elastic Container Registry (ECR)
echo "Creating Elastic Container Registry (ECR)..."
ECR_REPO_URI=$(aws ecr create-repository --repository-name "$ECR_NAME" -tags Key=Name,Value="$ECR_NAME" Key=Owner,Value="$OWNER" Key=Project,Value="$PROJECT" --region "$REGION" --query 'repository.repositoryUri' --output text)

export ECR_REPO_URI

if [ -z "$ECR_REPO_URI" ]; then
    echo "Error during ECR creation."
    exit 1
fi
echo "ECR repository created: $ECR_REPO_URI"

# Create Security Group
echo "Creating Security Group..."
SECURITY_GROUP_ID=$(aws ec2 create-security-group --group-name "$SECURITY_GROUP_NAME" --description "Security group for devOps internship assesment" --vpc-id "$VPC_ID" --region "$REGION" --output text)

if [ -z "$SECURITY_GROUP_ID" ]; then
    echo "Error during Security Group creation."
    exit 1
fi
echo "Security Group with ID $SECURITY_GROUP_ID has been created."

# Add tags to Security Group
aws ec2 create-tags --resources "$SECURITY_GROUP_ID" --tags Key=Name,Value="$SECURITY_GROUP_NAME" Key=Owner,Value="$OWNER" Key=Project,Value="$PROJECT" --region "$REGION"
echo "Security Group is now correctly configured."

# Allow inbound SSH access (port 22) from anywhere
aws ec2 authorize-security-group-ingress --group-id "$SECURITY_GROUP_ID" --protocol tcp --port 22 --cidr 0.0.0.0/0 --region "$REGION"
echo "Inbound SSH access has been allowed for Security Group."

# Create EC2 instance
echo "Creating EC2 instance..."

# UserData script to install Docker and run it
USER_DATA_SCRIPT=$(cat <<EOF
#!/bin/bash
sudo yum update -y
sudo yum install -y docker
sudo service docker start
EOF
)

INSTANCE_ID=$(aws ec2 run-instances --image-id ami-0ac67a26390dc374d --count 1 --instance-type t3.micro --key-name "$KEY_PAIR_NAME" --security-group-ids "$SECURITY_GROUP_ID" --subnet-id "$SUBNET_ID" --region "$REGION" --user-data "$USER_DATA_SCRIPT" --query 'Instances[0].InstanceId' --output text)
export INSTANCE_ID

if [ -z "$INSTANCE_ID" ]; then
    echo "Error during EC2 instance creation."
    exit 1
fi
echo "EC2 instance with ID $INSTANCE_ID has been created."

# Add tags to EC2 instance
aws ec2 create-tags --resources "$INSTANCE_ID" --tags Key=Name,Value="$INSTANCE_NAME" Key=Owner,Value="$OWNER" Key=Project,Value="$PROJECT" --region "$REGION"
echo "EC2 instance is now correctly configured."

# Allocate and associate public IP address with EC2 instance
echo "Allocating and associating public IP address with EC2 instance..."

PUBLIC_IP=$(aws ec2 allocate-address --domain vpc --region "$REGION" --output text)
export PUBLIC_IP

aws ec2 associate-address --instance-id "$INSTANCE_ID" --public-ip "$PUBLIC_IP" --region "$REGION"
echo "Public IP address has been allocated and associated with EC2 instance: $PUBLIC_IP"

echo "EC2 instance, public IP address, and Security Group have been successfully created."

./send_image_to_aws.sh
