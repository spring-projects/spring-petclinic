#!/bin/bash
set -e

echo "================================"
echo "CI Environment Validation"
echo "================================"

echo "Git:"
git --version

echo "Java:"
java -version

echo "Maven:"
./mvnw -version

echo "Docker:"
docker --version

echo "Terraform:"
terraform version

echo "Kubectl:"
kubectl version --client

echo "================================"
echo "Validation successful"
echo "================================"
