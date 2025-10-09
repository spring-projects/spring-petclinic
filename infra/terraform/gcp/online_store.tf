resource "google_redis_instance" "online_store" {
  project        = var.gcp_project_name
  region         = var.region
  name           = "${var.name_prefix}-online-store"
  tier           = var.redis_tier
  memory_size_gb = var.redis_memory_size_gb

  authorized_network = data.google_compute_network.redis-network.id

  redis_version = "REDIS_5_0"
  display_name  = "Feast Online Store"

}

data "google_compute_network" "redis-network" {
  project = var.gcp_project_name
  name    = var.network
}
