terraform {
  required_version = ">= 1.6.0"

  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 4.0"
    }
  }
}

provider "azurerm" {
  features {}

  subscription_id = var.subscription_id
}

resource "azurerm_resource_group" "main" {
  name     = var.resource_group_name
  location = var.location

  tags = {
    project     = "spring-petclinic"
    environment = "devops"
    managed_by  = "terraform"
  }
}

# ============================================================
# NETWORKING
# ============================================================

module "networking" {
  source = "./modules/networking"

  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location

  vnet_name          = "spring-petclinic-devops-vnet"
  vnet_address_space = ["10.50.0.0/16"]

  aca_subnet_name             = "aca-subnet"
  aca_subnet_address_prefixes = ["10.50.1.0/24"]

  aks_subnet_name             = "aks-subnet"
  aks_subnet_address_prefixes = ["10.50.2.0/23"]
}

# ============================================================
# STORAGE
# ============================================================

module "storage" {
  source = "./modules/storage"

  name                = "springpetclinicst87345"
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location
}

# ============================================================
# AZURE CONTAINER REGISTRY
# ============================================================

module "acr" {
  source = "./modules/acr"

  name                = "springpetclinicacr87345"
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location
}

# ============================================================
# VM
# ============================================================
