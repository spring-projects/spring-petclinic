variable "myregion" {
  type        = string
  default     = "ap-south-1"
}

variable "my_vpc" {
  type = string
}

variable "myvpctag" {
  type = string
}

variable "mypubsubnet" {
  type = string
}

variable "publicsubnettag" {
  type = string
}

variable "myintgwtag" {
  type = string
}

variable "mycidr_block" {
  type = string
}

variable "resource_version" {
  type = string
  default = "1.0"
}


variable "instance_type" {
  type = string
}

variable "ami_id" {
  type = string
}

