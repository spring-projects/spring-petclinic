#!/usr/bin/env bash
# -------------------
# This script removes AWS environment from previous scripts
# It removes: VPC, Subnet, Elastic Container Registry (ECR), EC2 instance with a public IP, Security Groups
#
# Required: configured AWS CLI

# Global variable
ERR=0

# User info
read -p "Enter region remove resources from: " REGION
read -p "Enter tag key to remove resources from: " TAG_KEY
read -p "Enter tag value to remove resources from: " TAG_VALUE
read -p "Enter ECR name to remove it from AWS: " ECR_NAME


echo "---------------------------------------"
echo ""
# Deleting EC2 Instance
echo "Deleting EC2 Instances..."
instances=$(aws ec2 describe-instances --region "$REGION" --query "Reservations[].Instances[?Tags[?Key=='$TAG_KEY'&&Value=='$TAG_VALUE']].InstanceId" --output text)

if [ -z "$instances" ]; then
    echo "There are no EC2 instances for tag $TAG_KEY:$TAG_VALUE."
else
    for instance_id in $instances; do
        aws ec2 terminate-instances --region "$REGION" --instance-ids "$instance_id"
        if [ $? -eq 0 ]; then
            echo "Instance $instance_id terminated successfully."
        else
            echo "Error terminating instance $instance_id."
            ERR=$((ERR+1))
        fi
    done
fi
echo ""
echo "---------------------------------------"
echo ""


# Deleting Elastic Container Registry (ECR)
echo "Deleting Elastic Container Registries (ECR)..."
if aws ecr describe-repositories --repository-names "$ECR_NAME" --region "$REGION" > /dev/null 2>&1; then
    aws ecr delete-repository --region "$REGION" --repository-name "$ECR_NAME" --force
    if [ $? -eq 0 ]; then
        echo "ECR repository $ECR_NAME deleted successfully."
    else
        echo "Error deleting ECR repository $ECR_NAME."
        ERR=$((ERR+1))
    fi
else
    echo "ECR repository $ECR_NAME does not exist."
fi
echo ""
echo "---------------------------------------"
echo ""


# Deleting VPC
echo "Deleting VPCs..."
for vpc_id in $(aws ec2 describe-vpcs --region "$REGION" --query "Vpcs[?Tags[?Key=='$TAG_KEY'&&Value=='$TAG_VALUE']].VpcId" --output text); do
    aws ec2 delete-vpc --region "$REGION" --vpc-id "$vpc_id"
    if [ $? -eq 0 ]; then
        echo "VPC $vpc_id deleted successfully."
    else
        echo "Error deleting VPC $vpc_id."
        ERR=$((ERR+1))
    fi
done

echo ""
echo "---------------------------------------"
echo ""

if [ $ERR -gt 0 ]; then
    echo "Not all resources were deleted successfully. Please check them manually."
else
    echo "All resources with tag $TAG_KEY:$TAG_VALUE have been successfully deleted from AWS."
fi
