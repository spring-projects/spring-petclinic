resource "google_storage_bucket" "dataproc_staging_bucket" {
  name          = var.dataproc_staging_bucket
  project       = var.gcp_project_name
  location      = var.region
  force_destroy = true
}

resource "google_dataproc_autoscaling_policy" "feast_dataproc_cluster_asp" {
  policy_id = var.name_prefix
  location  = var.region
  project   = var.gcp_project_name

  worker_config {
    min_instances = var.min_dataproc_worker_count
    max_instances = var.max_dataproc_worker_count
  }

  basic_algorithm {
    yarn_config {
      graceful_decommission_timeout = "3600s"
      scale_down_factor             = 0.5
      scale_up_factor               = 0.5
    }
  }
}

resource "google_dataproc_cluster" "feast_dataproc_cluster" {
  project = var.gcp_project_name
  name    = var.name_prefix
  region  = var.region

  cluster_config {
    staging_bucket = google_storage_bucket.dataproc_staging_bucket.name

    master_config {
      num_instances = 1
      machine_type  = var.dataproc_master_instance_type
      disk_config {
        boot_disk_type    = var.dataproc_master_disk_type
        boot_disk_size_gb = var.dataproc_master_disk_size
      }
    }

    worker_config {
      num_instances = var.min_dataproc_worker_count
      machine_type  = var.dataproc_worker_instance_type
      disk_config {
        boot_disk_type    = var.dataproc_worker_disk_type
        boot_disk_size_gb = var.dataproc_worker_disk_size
      }
    }

    gce_cluster_config {
      subnetwork      = var.subnetwork
      service_account = google_service_account.feast_sa.email

      internal_ip_only = true
    }

    software_config {
      image_version = var.dataproc_image_version
    }

    autoscaling_config {
      policy_uri = google_dataproc_autoscaling_policy.feast_dataproc_cluster_asp.name
    }
  }
}
