variable "project_id" {
  description = "The project ID to host the cluster in"
  default     = "github-actions-gcp"
}
variable "cluster_name" {
  description = "The name for the GKE cluster"
  default     = "gke0"
}
variable "env_name" {
  description = "The environment for the GKE cluster"
  default     = "test"
}
variable "region" {
  description = "The region to host the cluster in"
  default     = "europe-west1"
}
variable "network" {
  description = "The VPC network created to host the cluster in"
  default     = "gke-network"
}
variable "subnetwork" {
  description = "The subnetwork created to host the cluster in"
  default     = "gke-subnet"
}
variable "ip_range_pods_name" {
  description = "The secondary ip range to use for pods"
  default     = "ip-range-pods"
}
variable "ip_range_services_name" {
  description = "The secondary ip range to use for services"
  default     = "ip-range-services"
}
