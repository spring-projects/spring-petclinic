variable "name" {
  description = "Name of the Linux virtual machine"
  type        = string
}

variable "resource_group_name" {
  description = "Azure resource group name"
  type        = string
}

variable "location" {
  description = "Azure region"
  type        = string
}

variable "subnet_id" {
  description = "Subnet ID for the VM network interface"
  type        = string
}

variable "admin_username" {
  description = "Linux VM administrator username"
  type        = string
}

variable "ssh_public_key" {
  description = "SSH public key for VM authentication"
  type        = string
  sensitive   = true
}
