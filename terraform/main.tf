provider "google" {
  project = var.project_id
  region  = var.region
}

data "google_compute_default_service_account" "default" {}

data "google_compute_network" "default" {
  name = "default"
}

resource "google_compute_instance" "petclinic" {
  name         = var.vm_name
  machine_type = var.vm_type
  zone         = var.zone

  boot_disk {
    initialize_params {
      image = var.vm_image
      labels = {
        my_label = "value"
      }
    }
  }

  tags = ["http-server", "https-server"]

  network_interface {
    network = data.google_compute_network.default.self_link
  }

  metadata_startup_script = "${file("startup-script.sh")}"

  service_account {
    email  = data.google_compute_default_service_account.default.email
    scopes = ["cloud-platform"]
  }
}

resource "google_sql_database_instance" "petclinic" {
  name             = var.db_name
  database_version = var.db_version
  region           = var.region

  settings {
    tier = var.db_tier
    
    ip_configuration {
      ipv4_enabled     = false
      private_network  = data.google_compute_network.default.self_link
    }
  }
}

resource "google_sql_user" "users" {
  name     = var.app
  instance = google_sql_database_instance.petclinic.name
  password = "changeme"
}

resource "google_sql_database" "database" {
  name     = var.app
  instance = google_sql_database_instance.petclinic.name
}

resource "google_dns_managed_zone" "cloudsql" {
  name        = "cloudsql"
  dns_name    = "cloudsql.private."
  description = "Private DNS zone for CloudSQL"
  visibility  = "private"

  private_visibility_config {
    networks {
      network_url = data.google_compute_network.default.self_link
    }
  }
}

resource "google_dns_record_set" "petclinic" {
  name = "petclinic.${google_dns_managed_zone.cloudsql.dns_name}"
  type = "A"
  ttl  = 300

  managed_zone = google_dns_managed_zone.cloudsql.name

  rrdatas = [google_sql_database_instance.petclinic.ip_address[0].ip_address]
}

resource "google_compute_router" "router" {
  name    = "my-router"
  region  = var.region
  network = data.google_compute_network.default.self_link
}

resource "google_compute_router_nat" "nat" {
  name                               = "my-router-nat"
  router                             = google_compute_router.router.name
  region                             = var.region
  nat_ip_allocate_option             = "AUTO_ONLY"
  source_subnetwork_ip_ranges_to_nat = "ALL_SUBNETWORKS_ALL_IP_RANGES"

  log_config {
    enable = true
    filter = "ERRORS_ONLY"
  }
}