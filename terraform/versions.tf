terraform {
  required_providers {
    aws = {
      source = "hashicorp/aws"
    }
  }
}
provider "aws" {
  region = "us-east-1"
  access_key = "<your_aws_access_key>"
  secret_key = "<your_aws_secret_key>"
}