output "vm_id" {
  description = "Linux VM resource ID"
  value       = azurerm_linux_virtual_machine.main.id
}

output "vm_name" {
  description = "Linux VM name"
  value       = azurerm_linux_virtual_machine.main.name
}

output "public_ip_address" {
  description = "Public IP address of the VM"
  value       = azurerm_public_ip.main.ip_address
}

output "private_ip_address" {
  description = "Private IP address of the VM"
  value       = azurerm_network_interface.main.private_ip_address
}
