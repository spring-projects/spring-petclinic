resource "azurerm_container_registry" "main" {
  name                = var.name
  resource_group_name = var.resource_group_name
  location            = var.location

  sku           = "Basic"
  admin_enabled = false

  tags = {
    project    = "spring-petclinic"
    managed_by = "terraform"
  }
}
