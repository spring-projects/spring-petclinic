#!/usr/bin/env bash
# -------------------
# This script sets up basic AWS environment for pushing docker images into the cloud
# It creates: VPC, Subnet, Elastic Container Registry (ECR), EC2 instance with a public IP, Security Groups
#
# Required: Docker, configured AWS CLI, EC2 key pair created from AWS

# Global data
REGION="eu-west-1"
VPC_ID=""
SUBNET_ID=""
SECURITY_GROUP_ID=""
ECR_REPO_URI=""
INSTANCE_ID=""

# Get data from user - set it as env to be used in later scripts
echo "---------------------------------------"
echo ""
read -p "Enter owner name: " OWNER && export OWNER
read -p "Enter VPC name: " VPC_NAME && export VPC_NAME
read -p "Enter project name: " PROJECT && export PROJECT
read -p "Enter ECR repository name: " ECR_NAME && export ECR_NAME
read -p "Enter EC2 instance name: " INSTANCE_NAME && export INSTANCE_NAME
read -p "Enter key pair name: " KEY_PAIR_NAME && export KEY_PAIR_NAME
read -p "Enter security group name: " SECURITY_GROUP_NAME && export SECURITY_GROUP_NAME
read -p "Enter Elastic IP name: " EIP_NAME && export EIP_NAME


echo ""
echo "---------------------------------------"
echo ""


echo "Creating VPC..."
VPC_ID=$(aws ec2 create-vpc \
    --cidr-block 10.0.0.0/16 \
    --region "$REGION" \
    --tag-specifications 'ResourceType=vpc,Tags=[{Key=Name,Value='"$VPC_NAME"'},{Key=Owner,Value='"$OWNER"'},{Key=Project,Value='"$PROJECT"'}]' \
    --query 'Vpc.VpcId' \
    --output text)

if [ -z "$VPC_ID" ]; then
    echo "Error during VPC creation."
    exit 1
fi
echo "VPC with ID $VPC_ID has been created and tagged."

echo "VPC is now correctly configured."

echo ""
echo "---------------------------------------"
echo ""


# Create Subnet
echo "Creating Subnet..."
SUBNET_ID=$(aws ec2 create-subnet \
    --vpc-id "$VPC_ID" \
    --cidr-block 10.0.0.0/24 \
    --availability-zone "$REGION"a \
    --tag-specifications 'ResourceType=subnet,Tags=[{Key=Name,Value='"$VPC_NAME"'},{Key=Owner,Value='"$OWNER"'},{Key=Project,Value='"$PROJECT"'}]' \
    --query 'Subnet.SubnetId' \
    --output text)

if [ -z "$SUBNET_ID" ]; then
    echo "Error during Subnet creation."
    exit 1
fi
echo "Subnet with ID $SUBNET_ID has been created and tagged."

echo "Subnet is now correctly configured."

echo ""
echo "---------------------------------------"
echo ""


# Create Elastic Container Registry (ECR)
echo "Creating Elastic Container Registry (ECR)..."
ECR_REPO_JSON=$(aws ecr create-repository \
    --repository-name "$ECR_NAME" \
    --region "$REGION" \
    --query 'repository' \
    --output json)
ECR_REPO_URI=$(echo "$ECR_REPO_JSON" | jq -r '.repositoryUri')
ECR_REPO_ARN=$(echo "$ECR_REPO_JSON" | jq -r '.repositoryArn')

if [ -z "$ECR_REPO_URI" ] || [ -z "$ECR_REPO_ARN" ]; then
    echo "Error during ECR creation."
    exit 1
fi
echo "ECR repository created: $ECR_REPO_URI"

# Adding tags to the ECR repository
aws ecr tag-resource \
    --resource-arn "$ECR_REPO_ARN" \
    --tags Key=Name,Value="$ECR_NAME" Key=Owner,Value="$OWNER" Key=Project,Value="$PROJECT"

echo "Tags added to ECR repository."

echo ""
echo "---------------------------------------"
echo ""


# Create Security Group
echo "Creating Security Group..."
SECURITY_GROUP_ID=$(aws ec2 create-security-group \
    --description "Security group for devOps internship assesment" \
    --vpc-id "$VPC_ID" \
    --tag-specifications 'ResourceType=security-group,Tags=[{Key=Name,Value='"$SECURITY_GROUP_NAME"'},{Key=Owner,Value='"$OWNER"'},{Key=Project,Value='"$PROJECT"'}]' \
    --region "$REGION" \
    --output text)

if [ -z "$SECURITY_GROUP_ID" ]; then
    echo "Error during Security Group creation."
    exit 1
fi
echo "Security Group with ID $SECURITY_GROUP_ID has been created."

echo "Security Group is now correctly configured."

# Allow inbound SSH access (port 22) from anywhere
aws ec2 authorize-security-group-ingress \
    --group-id "$SECURITY_GROUP_ID" \
    --protocol tcp \
    --port 22 \
    --cidr 0.0.0.0/0 \
    --region "$REGION"
echo "Inbound SSH access has been allowed for Security Group."

echo ""
echo "---------------------------------------"
echo ""


# Create EC2 instance
echo "Creating EC2 instance..."

# UserData script to install Docker and run it
USER_DATA_SCRIPT=$(cat <<EOF
#!/bin/bash
sudo yum update -y
sudo yum install -y docker
sudo systemctl enable docker
sudo systemctl start docker
sudo usermod -aG docker $USER
newgrp docker
EOF
)

INSTANCE_ID=$(aws ec2 run-instances \
    --image-id ami-0ac67a26390dc374d \
    --count 1 \
    --instance-type t3.micro \
    --key-name "$KEY_PAIR_NAME" \
    --security-group-ids "$SECURITY_GROUP_ID" \
    --subnet-id "$SUBNET_ID" \
    --region "$REGION" \
    --user-data "$USER_DATA_SCRIPT" \
    --iam-instance-profile Name=allow_ec2_ecr \
    --tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value='"$VPC_NAME"'},{Key=Owner,Value='"$OWNER"'},{Key=Project,Value='"$PROJECT"'}]' \
    --query 'Instances[0].InstanceId' \
    --output text)
export INSTANCE_ID

if [ -z "$INSTANCE_ID" ]; then
    echo "Error during EC2 instance creation."
    exit 1
fi
echo "EC2 instance with ID $INSTANCE_ID has been created."

echo "EC2 instance is now correctly configured."

echo ""
echo "---------------------------------------"
echo ""


# Allocate and associate public IP address with EC2 instance
echo "Allocating and associating public IP address with EC2 instance..."

EIP_ALLOCATION_JSON=$(aws ec2 allocate-address \
    --domain vpc \
    --region "$REGION" \
    --tag-specifications 'ResourceType=elastic-ip,Tags=[{Key=Name,Value='"$VPC_NAME"'},{Key=Owner,Value='"$OWNER"'},{Key=Project,Value='"$PROJECT"'}]' \
    --output json)

# Check if the allocation was successful
if [ $? -ne 0 ]; then
    echo "Error during EIP allocation."
    exit 1
fi

# Extract the AllocationId and PublicIp using jq
EIP_ALLOCATION_ID=$(echo "$EIP_ALLOCATION_JSON" | jq -r '.AllocationId')
PUBLIC_IP=$(echo "$EIP_ALLOCATION_JSON" | jq -r '.PublicIp')

if [ -z "$EIP_ALLOCATION_ID" ] || [ -z "$PUBLIC_IP" ]; then
    echo "Error: Unable to retrieve EIP details."
    exit 1
fi

echo "Public IP allocated: $PUBLIC_IP with Allocation ID: $EIP_ALLOCATION_ID"

# Associate the allocated Elastic IP with the EC2 instance
aws ec2 associate-address --instance-id "$INSTANCE_ID" --allocation-id "$EIP_ALLOCATION_ID" --region "$REGION"

if [ $? -ne 0 ]; then
    echo "Error during EIP association."
    exit 1
fi

echo "Public IP address has been associated with EC2 instance: $PUBLIC_IP"

echo "Tags added to Elastic IP."


echo "EC2 instance, public IP address, and Security Group have been successfully created."

./send_image_to_aws.sh