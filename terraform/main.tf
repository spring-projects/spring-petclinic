provider "aws" {
  region = var.aws_region
}

# S3 bucket for storing artifacts
resource "aws_s3_bucket" "artifacts" {
  bucket = "petclinic-${var.environment}-artifacts"
  
  tags = {
    Name        = "PetClinic Artifacts"
    Environment = var.environment
  }
}

# ECR repository for Docker images
resource "aws_ecr_repository" "petclinic" {
  name = "petclinic-${var.environment}"
  
  image_scanning_configuration {
    scan_on_push = true
  }
  
  tags = {
    Name        = "PetClinic Docker Repository"
    Environment = var.environment
  }
}

# ECS cluster
resource "aws_ecs_cluster" "petclinic" {
  name = "petclinic-${var.environment}"
  
  setting {
    name  = "containerInsights"
    value = "enabled"
  }
  
  tags = {
    Name        = "PetClinic Cluster"
    Environment = var.environment
  }
}

# Security group for the ECS tasks
resource "aws_security_group" "ecs_tasks" {
  name        = "petclinic-${var.environment}-tasks-sg"
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
    Environment = var.environment
  }
}

# RDS Database for PetClinic
resource "aws_db_instance" "petclinic" {
  identifier           = "petclinic-${var.environment}db"
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
    Environment = var.environment
  }
}

output "ecr_repository_name" {
  value       = aws_ecr_repository.petclinic.name
  description = "Name of the PetClinic ECR repository"
}