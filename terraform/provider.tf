terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "6.27.0"
    }
  } /*
    backend "s3" {
    bucket         = "my-terraform-state-bucket"
    key            = "springboot/dev/terraform.tfstate"
    region         = "us-east-1"
    encrypt        = true
  } */
}

provider "aws" {
  region     = "us-east-1"
}