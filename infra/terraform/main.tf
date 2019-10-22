provider "azurerm" {
  version = "~>1.5"
}

terraform {
  backend "azurerm" {
    storage_account_name  = "jenkinsdemo1"
    container_name        = "tfstate"
    key                   = "terraform.tfstate"
  }
}
