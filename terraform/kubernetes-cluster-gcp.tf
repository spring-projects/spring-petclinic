# Empty cluster with two slave nodes and one master node, maybe as instance-small or instance-medium ones
resource "google_container_cluster" "primary" {
  depends_on = [google_project_service.api_8_container]

  name     = "terr-gke-cluster"
  location = var.zone #zonal cluster instead of regional

  network = google_compute_network.terr_vpc_1.id
  subnetwork = google_compute_subnetwork.terr_sub_vpc_1.id


  remove_default_node_pool = true
  initial_node_count       = 1

  ip_allocation_policy {
    cluster_secondary_range_name  = "services-range"
    services_secondary_range_name = google_compute_subnetwork.terr_sub_vpc_1.secondary_ip_range.1.range_name
  }
}

resource "google_container_node_pool" "primary_preemptible_nodes" {
  name       = "terr-node-pool"
  location   = var.zone #zonal cluster instead of regional
  cluster    = google_container_cluster.primary.name
  node_count = 1

  node_config {
    preemptible  = true
    machine_type = "e2-medium"

    # Google recommends custom service accounts that have cloud-platform scope and permissions granted via IAM Roles.
    service_account = google_service_account.custom_service_account_1.email
    oauth_scopes    = [
      "https://www.googleapis.com/auth/cloud-platform"
    ]
  }
}