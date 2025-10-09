module "redis" {
  source                  = "git::https://github.com/cloudposse/terraform-aws-elasticache-redis.git?ref=tags/0.25.0"
  subnets                 = module.vpc.private_subnets
  name                    = "${var.name_prefix}-online"
  vpc_id                  = module.vpc.vpc_id
  allowed_security_groups = [aws_security_group.all_worker_mgmt.id]
  availability_zones      = module.vpc.azs

  tags = var.tags
}