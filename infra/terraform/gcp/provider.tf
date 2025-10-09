provider "google" {
  version = "~> 3.46"
  project = var.gcp_project_name
}

data "google_client_config" "gcp_client" {
  provider = google
}

provider "kubernetes" {
  version = "~> 1.13.3"

  host                   = google_container_cluster.feast_gke_cluster.endpoint
  token                  = data.google_client_config.gcp_client.access_token
  cluster_ca_certificate = base64decode(google_container_cluster.feast_gke_cluster.master_auth.0.cluster_ca_certificate)
  load_config_file       = false
}

provider "helm" {
  version = "~> 1.3.2"
  kubernetes {
    host                   = google_container_cluster.feast_gke_cluster.endpoint
    token                  = data.google_client_config.gcp_client.access_token
    cluster_ca_certificate = base64decode(google_container_cluster.feast_gke_cluster.master_auth.0.cluster_ca_certificate)
    load_config_file       = false
  }
}
