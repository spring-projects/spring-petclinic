provider "aws" {
  region = "us-east-1"
}

variable "image_tag" {
  type = string
}

variable "environment" {
  type = string
}

variable "dockerhub_username" {
  type = string
}

# ECS Clusters
resource "aws_ecs_cluster" "staging_cluster" {
  count = var.environment == "staging" ? 1 : 0
  name  = "petclinic-staging-cluster"
}

resource "aws_ecs_cluster" "prod_cluster" {
  count = var.environment == "production" ? 1 : 0
  name  = "petclinic-prod-cluster"
}

# CloudWatch Log Group
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
  cpu                      = "256"  # Compatible with small instance type
  memory                   = "512"  # Compatible with small instance type
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
  task_role_arn            = aws_iam_role.ecs_task_execution_role.arn  # Use LabRole equivalent

  container_definitions = jsonencode([{
    name  = "petclinic"
    image = "${var.dockerhub_username}/petclinic:${var.image_tag}"
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
  task_definition = aws_ecs_task_definition.petclinic_task.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = ["subnet-12345678"]  # Replace with your Learner Lab subnet IDs
    security_groups  = ["sg-12345678"]      # Replace with your Learner Lab security group ID
    assign_public_ip = true
  }
}

# IAM Role for ECS Task Execution (LabRole assumed)
resource "aws_iam_role" "ecs_task_execution_role" {
  name = "LabRole"  # Must match Learner Lab's LabRole name
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
  # Note: In Learner Lab, this role already exists; Terraform might fail if it tries to recreate it
  # Use an existing role reference instead if needed (see below)
}

resource "aws_iam_role_policy_attachment" "ecs_task_execution_role_policy" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}
