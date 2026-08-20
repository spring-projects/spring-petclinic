output "resource_group_name" {
  description = "Name of the Spring PetClinic resource group"
  value       = azurerm_resource_group.main.name
}

output "resource_group_location" {
  description = "Location of the Spring PetClinic resource group"
  value       = azurerm_resource_group.main.location
}

output "vnet_name" {
  description = "Name of the Spring PetClinic virtual network"
  value       = module.networking.vnet_name
}

output "vnet_id" {
  description = "Resource ID of the Spring PetClinic virtual network"
  value       = module.networking.vnet_id
}

output "aca_subnet_id" {
  description = "Container Apps subnet ID"
  value       = module.networking.aca_subnet_id
}

output "aks_subnet_id" {
  description = "AKS subnet ID"
  value       = module.networking.aks_subnet_id
}

output "storage_account_id" {
  description = "Storage account resource ID"
  value       = module.storage.id
}

output "storage_account_name" {
  description = "Storage account name"
  value       = module.storage.name
}

output "storage_container_name" {
  description = "Storage container name"
  value       = module.storage.container_name
}

output "acr_id" {
  description = "Azure Container Registry resource ID"
  value       = module.acr.id
}

output "acr_name" {
  description = "Azure Container Registry name"
  value       = module.acr.name
}

output "acr_login_server" {
  description = "Azure Container Registry login server"
  value       = module.acr.login_server
}
