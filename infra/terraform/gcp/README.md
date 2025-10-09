# Terraform config for feast on GCP

This serves as a guide on how to deploy Feast on GCP. At the end of this guide, we will have provisioned:
1. GKE cluster
2. Feast services running on GKE
3. Google Memorystore (Redis) as online store
4. Dataproc cluster
4. Kafka running on GKE, exposed to the dataproc cluster via internal load balancer.

# Steps

1. Create a tfvars file, e.g. `my.tfvars`. A sample configuration is as below:

```
gcp_project_name        = "kf-feast"
name_prefix             = "feast-0-8"
region                  = "asia-east1"
gke_machine_type        = "n1-standard-2"
network                 = "default"
subnetwork              = "default"
dataproc_staging_bucket = "kf-feast-dataproc-staging-test"
```

3. Configure tf state backend, e.g.:
```
terraform {
  backend "gcs" {
    bucket = "<your bucket name>"
    prefix = "terraform/feast"
  }
}
```

3. Use `terraform apply -var-file="my.tfvars"` to deploy.

