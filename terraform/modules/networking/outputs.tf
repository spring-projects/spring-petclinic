output "vnet_id" {
  value = azurerm_virtual_network.main.id
}

output "vnet_name" {
  value = azurerm_virtual_network.main.name
}

output "aca_subnet_id" {
  value = azurerm_subnet.aca.id
}

output "aks_subnet_id" {
  value = azurerm_subnet.aks.id
}
