resource "azurerm_kubernetes_cluster" "main" {
  name = "${var.name_prefix}-aks"
  location = data.azurerm_resource_group.main.location
  resource_group_name = data.azurerm_resource_group.main.name
  dns_prefix = var.name_prefix
  default_node_pool {
    name = var.name_prefix
    vm_size = var.aks_machine_type
    node_count = var.aks_node_count
    vnet_subnet_id = azurerm_subnet.main.id
  }
  identity {
    type = "SystemAssigned"
  }
}
