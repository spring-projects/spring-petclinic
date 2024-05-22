#!/usr/bin/env bash
# -------------------
# This script removes AWS environment from previus scripts
# It removes: VPC, Subnet, Elastic Container Registry (ECR), EC2 instance with a public IP, Security Groups
#
# Reqiured: configured AWS CLI

read -p "Enter region remove resources from: " REGION
read -p "Enter tag key to remove resources from: " TAG_KEY
read -p "Enter tag value to remove resources from: " TAG_VALUE
read -p "Enter ECR name to remove it from aws: " ECR_NAME


echo "---------------------------------------"
echo ""
# Deleting EC2 Instance
echo "Deleting EC2 Instances..."
for instance_id in $(aws ec2 describe-instances --region "$REGION" --query "Reservations[].Instances[?Tags[?Key=='$TAG_KEY'&&Value=='$TAG_VALUE']].InstanceId" --output text); do
    aws ec2 terminate-instances --region "$REGION" --instance-ids "$instance_id"
    if [ $? -eq 0 ]; then
        echo "Instance $instance_id terminated successfully."
    else
        echo "Error terminating instance $instance_id."
    fi
done
echo ""
echo "---------------------------------------"
echo ""


# Deleting Elastic Container Registry (ECR)
echo "Deleting Elastic Container Registries (ECR)..."
aws ecr delete-repository --region "$REGION" --repository-name "$ECR_NAME" --force
if [ $? -eq 0 ]; then
    echo "ECR repository $ECR_NAME deleted successfully."
else
    echo "Error deleting ECR repository $ECR_NAME."
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
    fi
done

echo ""
echo "---------------------------------------"
echo ""

echo "All resources with tag $TAG_KEY:$TAG_VALUE have been successfully deleted from AWS."
