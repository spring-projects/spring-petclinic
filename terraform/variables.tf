variable "subscription_id" {
  description = "Azure subscription ID"
  type        = string
  sensitive   = true
}

variable "resource_group_name" {
  description = "Resource group for Spring PetClinic DevOps project"
  type        = string
  default     = "spring-petclinic-devops-rg"
}

variable "location" {
  description = "Azure region"
  type        = string
  default     = "centralindia"
}
