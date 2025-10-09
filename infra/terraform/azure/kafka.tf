resource "azurerm_hdinsight_kafka_cluster" "main" {
  name = "${var.name_prefix}-kafka"
  location = data.azurerm_resource_group.main.location
  resource_group_name = data.azurerm_resource_group.main.name
  cluster_version = "4.0"
  tier = "Standard"

  component_version {
    kafka = "2.1"
  }

  gateway {
    enabled = true
    username = "${var.name_prefix}-kafka-gateway"
    password = random_password.feast-kafka-gateway-password.result
  }

  storage_account {
    is_default = true
    storage_account_key = azurerm_storage_account.main.primary_access_key
    storage_container_id = azurerm_storage_container.kafka.id
  }

  roles {
    head_node {
      vm_size = var.kafka_head_vm_size
      username = "${var.name_prefix}-kafka-user"
      password = random_password.feast-kafka-role-password.result
      subnet_id = azurerm_subnet.kafka.id
      virtual_network_id = azurerm_virtual_network.main.id
    }
    worker_node {
      vm_size = var.kafka_worker_vm_size
      username = "${var.name_prefix}-kafka-user"
      password = random_password.feast-kafka-role-password.result
      number_of_disks_per_node = var.kafka_worker_disks_per_node
      target_instance_count = var.kafka_worker_target_instance_count
      subnet_id = azurerm_subnet.kafka.id
      virtual_network_id = azurerm_virtual_network.main.id
    }
    zookeeper_node {
      vm_size = var.kafka_zookeeper_vm_size
      username = "${var.name_prefix}-kafka-user"
      password = random_password.feast-kafka-role-password.result
      subnet_id = azurerm_subnet.kafka.id
      virtual_network_id = azurerm_virtual_network.main.id
    }
  }
}

resource "random_password" "feast-kafka-role-password" {
  length  = 16
  special = false
  min_upper = 1
  min_lower = 1
  min_numeric = 1
}

resource "random_password" "feast-kafka-gateway-password" {
  length  = 16
  special = true
  min_upper = 1
  min_lower = 1
  min_special = 1
  min_numeric = 1
}

resource "kubernetes_secret" "feast-kafka-gateway-secret" {
  metadata {
    name = "feast-kafka-gateway"
  }
  data = {
    kafka-gateway-password = random_password.feast-kafka-gateway-password.result
  }
}
