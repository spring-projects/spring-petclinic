#!/bin/bash
echo "Updating package list..."
sudo yum update -y

sudo yum install -y openjdk-21-jdk

echo "Installing Maven..."
sudo yum install -y maven


