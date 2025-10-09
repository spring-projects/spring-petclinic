# Terraform config for feast on AWS

Uses terraform 0.12

1. Run `aws emr create-default-roles` once.

2. Create a tfvars file, e.g. `my.tfvars` and set name_prefix:

```
name_prefix = "my-feast"
region      = "us-east-1"
```

3. Configure tf state backend, e.g.:
```
terraform {
  backend "s3" {
    bucket         = "my-terraform-state-bucket"
    key            = "clusters/my-feast-test"
    region         = "us-west-2"
    dynamodb_table = "terraform-state-lock"
    encrypt        = true
  }
}
```

3. Use `terraform apply -var-file="my.tfvars"` to deploy.

