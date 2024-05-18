#!/usr/bin/env bash
# -------------------
# This script removes AWS environment from previus scripts
# It removes: VPC, Subnet, Elastic Container Registry (ECR), EC2 instance with a public IP, Security Groups
#
# Reqiured: configured AWS CLI

read -p "Enter VPC id: " VPC_ID
read -p "Enter Subnet id: " SUBNET_ID
read -p "Enter ECR repository name: " ECR_NAME
read -p "Enter EC2 instance id: " INSTANCE_ID
read -p "Enter security group name: " SECURITY_GROUP_ID

# Deleting EC2 Instance
echo "Deleting EC2 Instance..."
aws ec2 terminate-instances --instance-ids "$INSTANCE_ID" --region "$REGION"
echo "EC2 Instance has been successfully deleted."

# Waiting for instance termination
echo "Waiting for instance termination..."
aws ec2 wait instance-terminated --instance-ids "$INSTANCE_ID" --region "$REGION"
echo "Instance termination has completed."

# Releasing public IP
echo "Releasing public IP..."
aws ec2 release-address --allocation-id "$(aws ec2 describe-instances --instance-ids "$INSTANCE_ID" --query 'Reservations[0].Instances[0].NetworkInterfaces[0].Association.AllocationId' --output text --region "$REGION")" --region "$REGION"
echo "Public IP has been successfully released."

# Deleting Elastic Container Registry (ECR)
echo "Deleting Elastic Container Registry (ECR)..."
aws ecr delete-repository --repository-name "$ECR_NAME" --force --region "$REGION"
echo "ECR Repository has been successfully deleted."

# Deleting Security Group
echo "Deleting Security Group..."
aws ec2 delete-security-group --group-id "$SECURITY_GROUP_ID" --region "$REGION"
echo "Security Group has been successfully deleted."

# Deleting Subnet
echo "Deleting Subnet..."
aws ec2 delete-subnet --subnet-id "$SUBNET_ID" --region "$REGION"
echo "Subnet has been successfully deleted."

# Deleting VPC
echo "Deleting VPC..."
aws ec2 delete-vpc --vpc-id "$VPC_ID" --region "$REGION"
echo "VPC has been successfully deleted."

echo "All resources have been successfully deleted from AWS."
