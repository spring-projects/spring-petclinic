variable "cidr_block" {
  default = "10.0.0.0/16"
  type    = string
}

variable "availability_zone" {
  default = "us-east-1a"
}

variable "cidr_block_public_subnet" {
  description = "Instance types for each workspace"
  type        = map(string)
  default = {
    dev  = "10.0.3.0/24"
    prod = "10.0.1.0/24"
  }
}

variable "cidr_block_private_subnet" {
  description = "subnet values"
  type = map(string)
  default = {
    dev = "10.0.4.0/24"
    prod = "10.0.2.0/24"
  }
  
}