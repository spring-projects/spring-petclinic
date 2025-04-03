provider "aws" {
  region = "us-east-1"
}

variable "image_tag" {
  type = string
}

variable "environment" {
  type = string
}

# ECR Repository (shared across environments)
resource "aws_ecr_repository" "petclinic" {
  name = "mtu/petclinic"
}

# ECS Clusters
# TODO: update cluster
resource "aws_ecs_cluster" "staging_cluster" {
  count = var.environment == "staging" ? 1 : 0
  name  = "petclinic-staging-cluster"
}

resource "aws_ecs_cluster" "prod_cluster" {
  count = var.environment == "production" ? 1 : 0
  name  = "petclinic-prod-cluster"
}

# CloudWatch Log Group
# TODO: update cluster
resource "aws_cloudwatch_log_group" "petclinic_logs" {
  name              = "/ecs/petclinic-${var.environment}"
  retention_in_days = 30
}

# CloudWatch Alarm
resource "aws_cloudwatch_metric_alarm" "high_cpu" {
  alarm_name          = "petclinic-${var.environment}-high-cpu"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = "2"
  metric_name         = "CPUUtilization"
  namespace           = "AWS/ECS"
  period              = "300"
  statistic           = "Average"
  threshold           = "80"
  alarm_description   = "This metric monitors ECS CPU utilization for ${var.environment}"
  alarm_actions       = [aws_sns_topic.alerts.arn]
  dimensions = {
    ClusterName = var.environment == "staging" ? aws_ecs_cluster.staging_cluster[0].name : aws_ecs_cluster.prod_cluster[0].name
    ServiceName = "petclinic-service-${var.environment}"
  }
}

# SNS Topic for Alarms
resource "aws_sns_topic" "alerts" {
  name = "petclinic-${var.environment}-alerts"
}

# ECS Task Definition
resource "aws_ecs_task_definition" "petclinic_task" {
  family                   = "petclinic-task-${var.environment}"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "256"
  memory                   = "512"
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn

  container_definitions = jsonencode([{
    name  = "petclinic"
    image = "215262883158.dkr.ecr.us-east-1.amazonaws.com/mtu/petclinic:${var.image_tag}"
    portMappings = [{
      containerPort = 8080
      hostPort      = 8080
    }]
    logConfiguration = {
      logDriver = "awslogs"
      options = {
        "awslogs-group"         = aws_cloudwatch_log_group.petclinic_logs.name
        "awslogs-region"        = "us-east-1"
        "awslogs-stream-prefix" = "ecs"
      }
    }
  }])
}

# ECS Service
resource "aws_ecs_service" "petclinic_service" {
  name            = "petclinic-service-${var.environment}"
  cluster         = var.environment == "staging" ? aws_ecs_cluster.staging_cluster[0].id : aws_ecs_cluster.prod_cluster[0].id
  task_definition               
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets         = ["subnet-12345678"]  # Replace with your subnet IDs
    security_groups = ["sg-12345678"]     # Replace with your security group ID
    assign_public_ip = true
  }
}

# IAM Role for ECS Task Execution
# TODO: use lab role (?)
resource "aws_iam_role" "ecs_task_execution_role" {
  name = "ecsTaskExecutionRole-${var.environment}"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action = "sts:AssumeRole"
      Effect = "Allow"
      Principal = {
        Service = "ecs-tasks.amazonaws.com"
      }
    }]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_task_execution_role_policy" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}
