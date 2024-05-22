#!/usr/bin/env bash
# -------------------
# This script removes AWS environment from previus scripts
# It removes: VPC, Subnet, Elastic Container Registry (ECR), EC2 instance with a public IP, Security Groups
#
# Reqiured: configured AWS CLI

read -p "Enter region remove resources from: " REGION
read -p "Enter Project tag key to remove resources from: " TAG_VALUE
read -p "Enter Project tag value to remove resources from: " TAG_KEY


echo "---------------------------------------"
echo ""
# Deleting EC2 Instance
echo "Deleting EC2 Instances..."
for instance_id in $(aws ec2 describe-instances --region "$REGION" --query "Reservations[].Instances[?Tags[?Key=='$TAG_KEY'&&Value=='$TAG_VALUE']].InstanceId" --output text); do
    aws ec2 terminate-instances --region "$REGION" --instance-ids "$instance_id"
done
echo ""
echo "---------------------------------------"
echo ""


# Deleting Elastic Container Registry (ECR)
echo "Deleting Elastic Container Registries (ECR)..."
for repo_name in $(aws ecr describe-repositories --region "$REGION" --query "repositories[?Tags[?Key=='$TAG_KEY'&&Value=='$TAG_VALUE']].repositoryName" --output text); do
    aws ecr delete-repository --region "$REGION" --repository-name "$repo_name" --force
done
echo ""
echo "---------------------------------------"
echo ""


# Deleting VPC
echo "Deleting VPCs..."
for vpc_id in $(aws ec2 describe-vpcs --region "$REGION" --query "Vpcs[?Tags[?Key=='$TAG_KEY'&&Value=='$TAG_VALUE']].VpcId" --output text); do
    aws ec2 delete-vpc --region "$REGION" --vpc-id "$vpc_id"
done

echo ""
echo "---------------------------------------"
echo ""

echo "All resources with tag $TAG_KEY:$TAG_VALUE have been successfully deleted from AWS."
