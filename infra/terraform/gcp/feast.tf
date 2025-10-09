locals {
  feast_postgres_secret_name = "${var.name_prefix}-postgres-secret"
  feast_helm_values = {
    redis = {
      enabled = false
    }

    kafka = {
      enabled = true
      externalAccess = {
        enabled = true
        service = {
          types                    = "LoadBalancer"
          port                     = 9094
          loadBalancerIPs          = [google_compute_address.kafka_broker.address]
          loadBalancerSourceRanges = ["10.0.0.0/8"]
          annotations = {
            "cloud.google.com/load-balancer-type" = "Internal"
          }
        }
      }
    }

    grafana = {
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
                host = google_redis_instance.online_store.host
                port = 6379
                subscriptions = [
                  {
                    name    = "*"
                    project = "*"
                  }
                ]
              }
            }
          ]
        }
      }
    }

    feast-jupyter = {
      enabled = true
      envOverrides = {
        feast_redis_host             = google_redis_instance.online_store.host,
        feast_redis_port             = 6379,
        feast_spark_launcher         = "dataproc"
        feast_dataproc_cluster_name  = google_dataproc_cluster.feast_dataproc_cluster.name
        feast_dataproc_project       = var.gcp_project_name
        feast_dataproc_region        = var.region
        feast_spark_staging_location = "gs://${var.dataproc_staging_bucket}/artifacts/"
        feast_historical_feature_output_location : "gs://${var.dataproc_staging_bucket}/out/"
        feast_historical_feature_output_format : "parquet"
        demo_kafka_brokers : "${google_compute_address.kafka_broker.address}:9094"
        demo_data_location : "gs://${var.dataproc_staging_bucket}/test-data/"
      }
      gcpServiceAccount = {
        enabled = true
        name    = var.feast_sa_secret_name
        key     = "credentials.json"
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

resource "google_compute_address" "kafka_broker" {
  project      = var.gcp_project_name
  region       = var.region
  subnetwork   = var.subnetwork
  name         = "${var.name_prefix}-kafka"
  address_type = "INTERNAL"
}

resource "helm_release" "feast" {
  depends_on = [kubernetes_secret.feast-postgres-secret, kubernetes_secret.feast_sa_secret]

  name  = var.name_prefix
  chart = "https://feast-helm-charts.storage.googleapis.com/feast-0.100.4.tgz"

  values = [
    yamlencode(local.feast_helm_values)
  ]
}
