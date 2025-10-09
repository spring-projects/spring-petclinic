resource "aws_security_group" "broker" {
  name_prefix = "${var.name_prefix}-kafka-broker"
  vpc_id      = module.vpc.vpc_id

  ingress {
    description     = "Allow connections from the worker group"
    security_groups = [aws_security_group.all_worker_mgmt.id]
    protocol        = "tcp"
    from_port       = 0
    to_port         = 65535
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = var.tags
}

resource "aws_msk_cluster" "msk" {
  cluster_name           = "${var.name_prefix}-kafka"
  kafka_version          = "2.4.1.1"
  number_of_broker_nodes = 2

  broker_node_group_info {
    instance_type   = "kafka.t3.small"
    ebs_volume_size = 100
    client_subnets  = [module.vpc.private_subnets[0], module.vpc.private_subnets[1]]
    security_groups = [aws_security_group.broker.id]
  }

  encryption_info {
    encryption_in_transit {
      client_broker = "TLS_PLAINTEXT"
    }
  }

  logging_info {
    broker_logs {
      s3 {
        enabled = true
        bucket  = aws_s3_bucket.feast_bucket.id
        prefix  = "msk-logs"
      }
    }
  }

  tags = var.tags
}
