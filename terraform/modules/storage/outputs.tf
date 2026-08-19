output "id" {
  value = azurerm_storage_account.main.id
}

output "name" {
  value = azurerm_storage_account.main.name
}

output "container_name" {
  value = azurerm_storage_container.artifacts.name
}
