data "azurerm_resource_group" "main" {
  name = var.resource_group
}

resource "azurerm_virtual_network" "main" {
  name = "${var.name_prefix}-vnet"
  location = data.azurerm_resource_group.main.location
  resource_group_name = data.azurerm_resource_group.main.name
  address_space = ["10.1.0.0/16"]
}

resource "azurerm_subnet" "main" {
  name = "${var.name_prefix}-aks-subnet"
  resource_group_name = data.azurerm_resource_group.main.name
  virtual_network_name = azurerm_virtual_network.main.name
  address_prefixes = ["10.1.0.0/24"]
}

resource "azurerm_subnet" "redis" {
  name = "${var.name_prefix}-redis-subnet"
  resource_group_name = data.azurerm_resource_group.main.name
  virtual_network_name = azurerm_virtual_network.main.name
  address_prefixes = ["10.1.128.0/24"]
}

resource "azurerm_subnet" "kafka" {
  name = "${var.name_prefix}-kafka-subnet"
  resource_group_name = data.azurerm_resource_group.main.name
  virtual_network_name = azurerm_virtual_network.main.name
  address_prefixes = ["10.1.64.0/24"]
}
