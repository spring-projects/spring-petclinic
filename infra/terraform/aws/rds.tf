resource "random_password" "db_password" {
  length           = 16
  special          = true
  override_special = "!#()-[]<>"
}

module "rds_cluster" {
  source          = "git::https://github.com/cloudposse/terraform-aws-rds-cluster.git?ref=tags/0.36.0"
  name            = "${var.name_prefix}-db"
  engine          = "aurora-postgresql"
  engine_mode     = "serverless"
  engine_version  = "10.7"
  cluster_family  = "aurora-postgresql10"
  cluster_size    = 0
  admin_user      = var.postgres_db_user
  admin_password  = random_password.db_password.result
  db_name         = var.postgres_db_name
  db_port         = 5432
  instance_type   = "db.t2.small"
  vpc_id          = module.vpc.vpc_id
  security_groups = [aws_security_group.all_worker_mgmt.id]
  subnets         = module.vpc.private_subnets

  scaling_configuration = [
    {
      auto_pause               = true
      max_capacity             = 16
      min_capacity             = 2
      seconds_until_auto_pause = 300
      timeout_action           = "ForceApplyCapacityChange"
    }
  ]

  tags = var.tags
}