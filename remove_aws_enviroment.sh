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
    echo "Deleting VPC: $vpc_id..."

    # Delete internet gateway
    igw_id=$(aws ec2 describe-internet-gateways --region "$REGION" --filters "Name=attachment.vpc-id,Values=$vpc_id" --query "InternetGateways[].InternetGatewayId" --output text)
    if [ -n "$igw_id" ]; then
        aws ec2 detach-internet-gateway --internet-gateway-id "$igw_id" --vpc-id "$vpc_id" --region "$REGION"
        aws ec2 delete-internet-gateway --internet-gateway-id "$igw_id" --region "$REGION"
    fi

    # Delete route table associations and route tables
    for rtb_id in $(aws ec2 describe-route-tables --region "$REGION" --filters "Name=vpc-id,Values=$vpc_id" --query "RouteTables[].RouteTableId" --output text); do
        aws ec2 disassociate-route-table --association-id "$(aws ec2 describe-route-tables --region "$REGION" --route-table-id "$rtb_id" --query "RouteTables[?VpcId=='$vpc_id'].Associations[].RouteTableAssociationId" --output text)" --region "$REGION"
        aws ec2 delete-route-table --route-table-id "$rtb_id" --region "$REGION"
    done

    # Delete subnets
    for subnet_id in $(aws ec2 describe-subnets --region "$REGION" --filters "Name=vpc-id,Values=$vpc_id" --query "Subnets[].SubnetId" --output text); do
        aws ec2 delete-subnet --subnet-id "$subnet_id" --region "$REGION"
    done

    # Delete network ACLs
    for nacl_id in $(aws ec2 describe-network-acls --region "$REGION" --filters "Name=vpc-id,Values=$vpc_id" --query "NetworkAcls[].NetworkAclId" --output text); do
        aws ec2 delete-network-acl --network-acl-id "$nacl_id" --region "$REGION"
    done

    # Delete security groups
    for sg_id in $(aws ec2 describe-security-groups --region "$REGION" --filters "Name=vpc-id,Values=$vpc_id" --query "SecurityGroups[].GroupId" --output text); do
        aws ec2 delete-security-group --group-id "$sg_id" --region "$REGION"
    done

    # Finally, delete VPC
    aws ec2 delete-vpc --vpc-id "$vpc_id" --region "$REGION"
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
