provider "helm" {
  kubernetes {
    host                   = data.aws_eks_cluster.cluster.endpoint
    cluster_ca_certificate = base64decode(data.aws_eks_cluster.cluster.certificate_authority.0.data)
    token                  = data.aws_eks_cluster_auth.cluster.token
    load_config_file       = false
  }
}

# Construct feast configs that need to point to RDS and Redis.
#
# RDS password is stored in a configmap which is not awesome but that RDS instance is not routable
# from the outside anyways so that'll do.
locals {
  feast_core_config = {
    redis = {
      enabled = false
    }
    postgresql = {
      enabled = false
    }
    kafka = {
      enabled = false
    }

    "feast-core" = {
      "application-generated.yaml" = {
        enabled = false
      }

      "application-override.yaml" = {
        spring = {
          datasource = {
            url      = "jdbc:postgresql://${module.rds_cluster.endpoint}:5432/${module.rds_cluster.database_name}"
            username = "${module.rds_cluster.master_username}"
            password = "${random_password.db_password.result}"
          }
        }
        feast = {
          stream = {
            type = "kafka"
            options = {
              bootstrapServers = aws_msk_cluster.msk.bootstrap_brokers
              topic            = "feast"
            }
          }
        }
        server = {
          port = "8080"
        }
      }
    }

    "feast-serving" = {
      "application-override.yaml" = {
        enabled = true
        feast = {
          stores = [
            {
              name = "online"
              type = "REDIS"
              config = {
                host = module.redis.endpoint
                port = 6379
                ssl  = true
              }
              subscriptions = [
                {
                  name    = "*"
                  project = "*"
                  version = "*"
                }
              ]
            }
          ]
          job_store = {
            redis_host = module.redis.endpoint
            redis_port = 6379
          }
        }
      }
    }

    "feast-jupyter" = {
      "envOverrides" = {
        feast_redis_host                         = module.redis.endpoint
        feast_redis_port                         = 6379
        feast_redis_ssl                          = true
        feast_emr_cluster_id                     = (length(aws_emr_cluster.persistent_cluster) > 0) ? aws_emr_cluster.persistent_cluster[0].id : null
        feast_emr_region                         = var.region
        feast_spark_staging_location             = "s3://${aws_s3_bucket.feast_bucket.id}/artifacts/"
        feast_emr_log_location                   = "s3://${aws_s3_bucket.feast_bucket.id}/emr-logs/"
        feast_spark_launcher                     = "emr"
        feast_historical_feature_output_location = "s3://${aws_s3_bucket.feast_bucket.id}/out/"
        feast_historical_feature_output_format   = "parquet"
        demo_kafka_brokers                       = aws_msk_cluster.msk.bootstrap_brokers
        demo_data_location                       = "s3://${aws_s3_bucket.feast_bucket.id}/test-data/"
      }
    }
  }
}

resource "helm_release" "feast" {
  name  = "feast"
  chart = "../../charts/feast"

  wait = false

  values = [
    yamlencode(local.feast_core_config)
  ]
}