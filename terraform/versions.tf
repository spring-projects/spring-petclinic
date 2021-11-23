terraform {
  required_providers {
    aws = {
      source = "hashicorp/aws"
    }
  }
}
provider "aws" {
  region = "us-east-1"
  access_key = ${jsonencode(var.aws_access_key)}
  secret_key = ${jsonencode(var.aws_secret_key)}
}