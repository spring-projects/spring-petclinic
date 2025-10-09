variable "resource_group" {
  type = string
}

variable "name_prefix" {
  type = string
}

variable "aks_machine_type" {
  type = string
  default = "Standard_DS2_v2"
}

variable "aks_node_count" {
  type = number
  default = 2
}

variable "redis_capacity" {
  type = number
  default = 2
}

variable "storage_account_replication_type" {
  type = string
  default = "LRS"
}

variable "aks_namespace" {
  type = string
  default = "default"
}

variable "kafka_head_vm_size" {
  type = string
  default = "Standard_DS3_v2"
}

variable "kafka_worker_vm_size" {
  type = string
  default = "A5"
}

variable "kafka_zookeeper_vm_size" {
  type = string
  default = "Standard_DS3_v2"
}

variable "kafka_worker_disks_per_node" {
  type = number
  default = 3
}

variable "kafka_worker_target_instance_count" {
  type = number
  default = 3
}
