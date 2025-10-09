data "aws_iam_instance_profile" "emr_default_role" {
  name = "EMR_EC2_DefaultRole"
}

resource "aws_emr_cluster" "persistent_cluster" {
  count = var.use_persistent_emr_cluster ? 1 : 0

  name                              = "${var.name_prefix}-persistent-emr"
  keep_job_flow_alive_when_no_steps = true
  release_label                     = "emr-6.0.0"

  ec2_attributes {
    subnet_id                         = module.vpc.private_subnets[0]
    additional_master_security_groups = aws_security_group.all_worker_mgmt.id
    additional_slave_security_groups  = aws_security_group.all_worker_mgmt.id
    instance_profile                  = data.aws_iam_instance_profile.emr_default_role.arn
  }

  applications = ["Hadoop", "Hive", "Spark", "Livy"]
  service_role = "EMR_DefaultRole"

  bootstrap_action {
    path = "s3://aws-bigdata-blog/artifacts/resize_storage/resize_storage.sh"
    name = "runif"
    args = ["--scaling-factor", "1.5"]
  }

  master_instance_fleet {
    instance_type_configs {
      instance_type = "m4.xlarge"
      ebs_config {
        size                 = "100"
        type                 = "gp2"
        volumes_per_instance = 1
      }
    }
    launch_specifications {
      spot_specification {
        timeout_action           = "SWITCH_TO_ON_DEMAND"
        timeout_duration_minutes = 10
        allocation_strategy      = "capacity-optimized"
      }
    }
    target_spot_capacity = 1
  }
  core_instance_fleet {
    instance_type_configs {
      bid_price_as_percentage_of_on_demand_price = 100
      ebs_config {
        size                 = "100"
        type                 = "gp2"
        volumes_per_instance = 1
      }
      instance_type     = "m4.xlarge"
      weighted_capacity = 1
    }
    launch_specifications {
      spot_specification {
        timeout_action           = "SWITCH_TO_ON_DEMAND"
        timeout_duration_minutes = 10
        allocation_strategy      = "capacity-optimized"
      }
    }
    target_spot_capacity = 2
  }

  step_concurrency_level = 256

  log_uri = "s3://${aws_s3_bucket.feast_bucket.id}/logs/${var.name_prefix}-persistent-emr/"

  tags = var.tags
}
