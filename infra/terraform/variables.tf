variable "client_id" {
  default = "764b3430-7053-4520-bbba-d74b8df5270c"
}
variable "client_secret" {
  default = "Z.4tiyJc/l]ahAi:XKI2Ox3xg5UXWXdC"
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
