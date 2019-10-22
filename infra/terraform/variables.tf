variable "client_id" {
  default = ""
}
variable "client_secret" {
  default = ""
}

variable "agent_count" {
  default = 3
}

variable "ssh_public_key" {
  default = "~/.ssh/id_rsa.pub"
}

variable "dns_prefix" {
  default = "k8stest"
}

variable cluster_name {
  default = "k8stest"
}

variable resource_group_name {
  default = "azure-k8stest"
}

variable location {
  default = "East US"
}
