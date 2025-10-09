variable "gcp_project_name" {
  description = "GCP project name"
}

variable "name_prefix" {
  description = "Prefix to be used when naming the different components of Feast"
}

variable "region" {
  description = "Region for GKE and Dataproc cluster"
}

variable "gke_machine_type" {
  description = "GKE node pool machine type"
  default     = "n1-standard-4"
}

variable "gke_node_count" {
  description = "Number of nodes in the GKE default node pool"
  default     = 1
}

variable "gke_disk_size_gb" {
  description = "Disk size for nodes in the GKE default node pool"
  default     = 100
}

variable "gke_disk_type" {
  description = "Disk type for nodes in the GKE default node pool"
  default     = "pd-standard"
}

variable "network" {
  description = "Network for GKE and Dataproc cluster"
}

variable "subnetwork" {
  description = "Subnetwork for GKE and Dataproc cluster"
}

variable "dataproc_staging_bucket" {
  description = "GCS bucket for staging temporary files required for dataproc jobs"
}

variable "min_dataproc_worker_count" {
  description = "Minimum dataproc worker count"
  default     = 2
}

variable "max_dataproc_worker_count" {
  description = "Maximum dataproc worker count"
  default     = 4
}

variable "dataproc_master_instance_type" {
  description = "Machine type for dataproc cluster master"
  default     = "n1-standard-2"
}

variable "dataproc_master_disk_type" {
  description = "Disk type for dataproc cluster master"
  default     = "pd-standard"
}

variable "dataproc_master_disk_size" {
  description = "Disk size for dataproc cluster master"
  default     = 100
}

variable "dataproc_worker_instance_type" {
  description = "Machine type for dataproc cluster worker"
  default     = "n1-standard-2"
}

variable "dataproc_worker_disk_type" {
  description = "Disk type for dataproc cluster worker"
  default     = "pd-standard"
}

variable "dataproc_worker_disk_size" {
  description = "Disk size for dataproc cluster worker"
  default     = 100
}

variable "dataproc_image_version" {
  description = "Dataproc image version"
  default     = "1.5-debian10"
}

variable "redis_tier" {
  description = "GCP Redis instance tier"
  default     = "BASIC"
}

variable "redis_memory_size_gb" {
  description = "Redis memory size in Gb"
  default     = 2
}

variable "feast_sa_secret_name" {
  description = "Kubernetes secret name for Feast GCP service account"
  default     = "feast-gcp-service-account"
}
