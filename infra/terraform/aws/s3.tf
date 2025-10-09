resource "random_string" "s3_suffix" {
  length  = 8
  lower   = true
  upper   = false
  special = false
}

resource "aws_s3_bucket" "feast_bucket" {
  # Since bucket names are globally unique, we add a random suffix here.
  bucket = "${var.name_prefix}-feast-${random_string.s3_suffix.result}"
  acl    = "private"

  server_side_encryption_configuration {
    rule {
      apply_server_side_encryption_by_default {
        sse_algorithm = "AES256"
      }
    }
  }

  tags = var.tags
}