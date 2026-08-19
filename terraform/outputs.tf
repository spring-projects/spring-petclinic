# ============================================================
# RESOURCE GROUP
# ============================================================

output "resource_group_name" {
  description = "Resource group name"
  value       = azurerm_resource_group.main.name
}

output "resource_group_location" {
  description = "Resource group location"
  value       = azurerm_resource_group.main.location
}

# ============================================================
# NETWORKING
# ============================================================

output "vnet_name" {
  description = "Virtual network name"
  value       = module.networking.vnet_name
}

output "aca_subnet_id" {
  description = "Azure Container Apps subnet ID"
  value       = module.networking.aca_subnet_id
}

output "aks_subnet_id" {
  description = "AKS subnet ID"
  value       = module.networking.aks_subnet_id
}

# ============================================================
# STORAGE
# ============================================================

output "storage_account_name" {
  description = "Storage account name"
  value       = module.storage.name
}

output "storage_container_name" {
  description = "Storage container name"
  value       = module.storage.container_name
}

# ============================================================
# ACR
# ============================================================

output "acr_name" {
  description = "Azure Container Registry name"
  value       = module.acr.name
}

output "acr_login_server" {
  description = "Azure Container Registry login server"
  value       = module.acr.login_server
}

# ============================================================
# VM
# ============================================================
