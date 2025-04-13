provider "aws" {
  region = var.aws_region
}

# Create a local value to ensure we always have a valid environment name
locals {
  # If environment is empty, default to "dev"
  env_name = var.environment != "" ? var.environment : "dev"
}

# S3 bucket for storing artifacts
resource "aws_s3_bucket" "artifacts" {
  bucket = "petclinic-${local.env_name}-artifacts"
  
  tags = {
    Name        = "PetClinic Artifacts"
    Environment = local.env_name
  }
}

# ECR repository for Docker images
resource "aws_ecr_repository" "petclinic" {
  name = "petclinic-${local.env_name}"
  
  image_scanning_configuration {
    scan_on_push = true
  }
  
  tags = {
    Name        = "PetClinic Docker Repository"
    Environment = local.env_name
  }
}

# ECS cluster
resource "aws_ecs_cluster" "petclinic" {
  name = "petclinic-${local.env_name}"
  
  setting {
    name  = "containerInsights"
    value = "enabled"
  }
  
  tags = {
    Name        = "PetClinic Cluster"
    Environment = local.env_name
  }
}

# Security group for the ECS tasks
resource "aws_security_group" "ecs_tasks" {
  name        = "petclinic-${local.env_name}-tasks-sg"
  description = "Allow inbound traffic to petclinic application"
  vpc_id      = var.vpc_id
  
  ingress {
    protocol        = "tcp"
    from_port       = 8080
    to_port         = 8080
    cidr_blocks     = ["0.0.0.0/0"]
  }
  
  egress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
  
  tags = {
    Name        = "PetClinic Tasks SG"
    Environment = local.env_name
  }
}

# RDS Database for PetClinic
resource "aws_db_instance" "petclinic" {
  identifier           = "petclinic-${local.env_name}db"
  allocated_storage    = 20
  storage_type         = "gp2"
  engine               = "mysql"
  engine_version       = "8.0"
  instance_class       = "db.t3.micro"
  username             = var.db_username
  password             = var.db_password
  parameter_group_name = "default.mysql8.0"
  skip_final_snapshot  = true
  
  tags = {
    Name        = "PetClinic Database"
    Environment = local.env_name
  }
}

output "ecr_repository_name" {
  value       = aws_ecr_repository.petclinic.name
  description = "Name of the PetClinic ECR repository"
}