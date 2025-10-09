locals {
  feast_postgres_secret_name = "${var.name_prefix}-postgres-secret"
  feast_helm_values = {
    redis = {
      enabled = false
    }

    grafana = {
      enabled = false
    }

    kafka = {
      enabled = false
    }

    postgresql = {
      existingSecret = local.feast_postgres_secret_name
    }

    feast-core = {
      postgresql = {
        existingSecret = local.feast_postgres_secret_name
      }
    }

    feast-serving = {
      enabled = true
      "application-override.yaml" = {
        feast = {
          core-host      = "${var.name_prefix}-feast-core"
          core-grpc-port = 6565
          active_store   = "online_store"
          stores = [
            {
              name = "online_store"
              type = "REDIS"
              config = {
                host = azurerm_redis_cache.main.hostname
                port = azurerm_redis_cache.main.ssl_port
                ssl  = true
              }
            }
          ]
        }
      }
    }

    feast-jupyter = {
      enabled = true
      envOverrides = {
        feast_redis_host             = azurerm_redis_cache.main.hostname,
        feast_redis_port             = azurerm_redis_cache.main.ssl_port,
        feast_redis_ssl              = true
        feast_spark_launcher         = "k8s"
        feast_spark_staging_location = "wasbs://${azurerm_storage_container.staging.name}@${azurerm_storage_account.main.name}.blob.core.windows.net/artifacts/"
        feast_historical_feature_output_location : "wasbs://${azurerm_storage_container.staging.name}@${azurerm_storage_account.main.name}.blob.core.windows.net/out/"
        feast_historical_feature_output_format : "parquet"
        demo_data_location : "wasbs://${azurerm_storage_container.staging.name}@${azurerm_storage_account.main.name}.blob.core.windows.net/test-data/"
        feast_azure_blob_account_name = azurerm_storage_account.main.name
        feast_azure_blob_account_access_key = azurerm_storage_account.main.primary_access_key
      }
    }
  }
}

resource "random_password" "feast-postgres-password" {
  length  = 16
  special = false
}

resource "kubernetes_secret" "feast-postgres-secret" {
  metadata {
    name = local.feast_postgres_secret_name
  }
  data = {
    postgresql-password = random_password.feast-postgres-password.result
  }
}

resource "helm_release" "feast" {
  depends_on = [kubernetes_secret.feast-postgres-secret]

  name  = var.name_prefix
  namespace = var.aks_namespace
  repository = "https://feast-helm-charts.storage.googleapis.com"
  chart = "feast"

  values = [
    yamlencode(local.feast_helm_values)
  ]
}

resource "helm_release" "sparkop" {
  name = "sparkop"
  namespace = "default"
  repository = "https://googlecloudplatform.github.io/spark-on-k8s-operator"
  chart = "spark-operator"
  set {
    name = "serviceAccounts.spark.name"
    value = "spark"
  }
}
