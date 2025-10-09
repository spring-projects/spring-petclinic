resource "azurerm_storage_account" "main" {
  name = "${var.name_prefix}storage"
  resource_group_name = data.azurerm_resource_group.main.name
  location = data.azurerm_resource_group.main.location
  account_kind = "StorageV2"
  account_tier = "Standard"
  account_replication_type = var.storage_account_replication_type
  allow_blob_public_access = true
}

resource "azurerm_storage_container" "staging" {
  name = "staging"
  storage_account_name = azurerm_storage_account.main.name
  container_access_type = "blob"
}

resource "azurerm_storage_container" "kafka" {
  name = "kafkastorage"
  storage_account_name = azurerm_storage_account.main.name
  container_access_type = "blob"
}
