variable "cidr_block" {
  default = "10.0.0.0/16"
  type    = string
}

variable "cidr_block_public_subnet" {
  default = "10.0.1.0/24"
}

variable "availability_zone" {
  default = "us-east-1a"
}


variable "cidr_block_private_subnet" {
  default = "10.0.2.0/24"
}