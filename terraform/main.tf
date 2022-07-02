terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "=3.0.0"
    }
  }
}

# Configure the Microsoft Azure Provider
provider "azurerm" {
  features {}

}

# Create a resource group
resource "azurerm_resource_group" "rg" {
  name     = "epam-project"
  location = "West Europe"
}

# Create a virtual network within the resource group
resource "azurerm_virtual_network" "vnc" {
  name                = "jenkins"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  address_space       = ["10.0.0.0/16"]
}

resource "azurerm_network_security_group" "nsg" {
  name                = "acceptanceTestSecurityGroup1"
  location            = azurerm_resource_group.rg.location
  resource_group_name = azurerm_resource_group.rg.name

  security_rule {
    name                       = "ci-cd-ssh"
    priority                   = 100
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "22"
    destination_port_range     = "22"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }

  security_rule {
    name                       = "ci-cd-tls"
    priority                   = 101
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "443"
    destination_port_range     = "443"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }

  security_rule {
    name                       = "ci-cd-http"
    priority                   = 102
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "80"
    destination_port_range     = "80"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }
}

resource "azurerm_subnet" "snet" {
  name                 = "internal"
  resource_group_name  = azurerm_resource_group.rg.name
  virtual_network_name = azurerm_virtual_network.vnc.name
  address_prefixes     = ["10.0.2.0/24"]
}

resource "azurerm_public_ip" "public-ip" {
  name                = "acceptanceTestPublicIp1"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  allocation_method   = "Dynamic"

  tags = {
    environment = "Production"
  }
}

resource "azurerm_network_interface" "nic" {
  name                = "nic"
  location            = azurerm_resource_group.rg.location
  resource_group_name = azurerm_resource_group.rg.name

  ip_configuration {
    name                          = "ip"
    subnet_id                     = azurerm_subnet.snet.id
    private_ip_address_allocation = "Dynamic"
    public_ip_address_id          = azurerm_public_ip.public-ip.id
  }
}

resource "azurerm_linux_virtual_machine" "vm" {
  name                            = "example-machine"
  resource_group_name             = azurerm_resource_group.rg.name
  location                        = azurerm_resource_group.rg.location
  size                            = "Standard_D2ads_v5"
  admin_username                  = "ubuntu"
  disable_password_authentication = true
  network_interface_ids = [
    azurerm_network_interface.nic.id,
  ]

  admin_ssh_key {
    username   = "ubuntu"
    public_key = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQDKvKkWebxJX6nYTS8f1dPd8SmfBykhSi3lqw3T9k7iy4oeg8b7ixbL4qeEaeFbGeB+f4Hf5JNpX8GsWk8a+B9BlACgxHWlPlX9DQGocs2aK3q8w3R1m1KPyShB3s1YtlaQ/aiXyyXIWx7zjK0ALlUvtqfUecCP4pUow5yLwg3Qvl7IocOJ3SaHztToqMzS22mGvjTCuLHch2ToW9zOniZmTPmcJ4jrNx7Uzc2FyZEGFM/1XDiFH6qUnwfIkMq7O8ulJ2YWuN+r0YVbmsHkghEHp5SqsT3MFbNHXxQKNlYt9w+18TpLjqFR+Q83k/RciSV0Tn6xkZVAB5/xOJqfq4SHTCOm57OLBjYN93Wb6msTKivKrMaWdUffurh691CiUjWeRc5TGwYy/AgQ5ymsx3rYne1ouOFyKTEyTdlps1WCN3+mpcRzDKw6O425AvKHtT4yBbHlSGtPfHQXi1YqioVv/rW5u28YHxAlm6TbztKZXoy9m5PVkkMVTz65M+NQ68E= b2q@thinkpad"


  }

  os_disk {
    caching              = "ReadWrite"
    storage_account_type = "Standard_LRS"
  }

  source_image_reference {
    publisher = "Canonical"
    offer     = "0001-com-ubuntu-server-focal"
    sku       = "20_04-lts-gen2"
    version   = "latest"
  }
}
