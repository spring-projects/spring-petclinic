variable "ami" {
  description = "AMI for ec2 instance"
}

variable "public_subnet_id" {
  description = "Public subnet id created in VPC module"
}

variable "instance_type" {
  type = map(string)
  default = {
    dev = "t2.medium",
    prod = "t3.medium"
  }
  description = "Instance type pf ec2 instance"
}

variable "vpc_id" {
  description = "VPC ID"
}