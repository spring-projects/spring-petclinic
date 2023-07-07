provider "google" {
  project = var.google_project
  region = var.google_region
  zone = var.google_zone
}

# NETWORK
resource "google_compute_network" "app_network" {
  name = var.app_network_name
  auto_create_subnetworks = false
}
# SUBNETWORK
resource "google_compute_subnetwork" "app_subnet_1" { # number for potencial scaling 
  name = "${var.app_subnet_name}-${var.google_region}"
  ip_cidr_range = "10.10.0.0/16"
  region = var.google_region
  network = google_compute_network.app_network.id
}


# PRIVATE SERVICE NETWORK - NEEDED FOR PRIVATE IP ACCESS TO CLOUDSQL INSTANCE 
resource "google_compute_global_address" "psa_ip_addresses" {
    name = "psa-ip-addresses"
    purpose = "VPC_PEERING"
    address_type = "INTERNAL"
    prefix_length = "24"
    network = google_compute_network.app_network.id
}

resource "google_service_networking_connection" "psa_connection" {
  network = google_compute_network.app_network.id
  service = "servicenetworking.googleapis.com"
  reserved_peering_ranges = [ google_compute_global_address.psa_ip_addresses.name ]
}
# FIREWALL
resource "google_compute_firewall" "ssh_access_app" {
  name    = "ssh-access-app"
  network = google_compute_network.app_network.id
  allow {
    protocol = "tcp"
    ports    = ["22"]
  }
  source_ranges = ["0.0.0.0/0"]
  target_tags   = ["ssh-access"]
}

resource "google_compute_firewall" "http_access_app" {
  name    = "http-access-app"
  network = google_compute_network.app_network.id
  allow {
    protocol = "tcp"
    ports    = ["80"]
  }
  source_ranges = ["0.0.0.0/0"]
  target_tags   = ["http-access"]
}

resource "google_compute_firewall" "java_8080_access_app" {
  name    = "java-8080-access-app"
  network = google_compute_network.app_network.id
  allow {
    protocol = "tcp"
    ports    = ["8080"]
  }
  source_ranges = ["0.0.0.0/0"]
  target_tags   = ["java-8080-access"]
}
# LOAD BALANCER
# reserve ip address
resource "google_compute_global_address" "lb_address" {
  name = "lb-address"
  address_type = "EXTERNAL"
}

resource "google_compute_global_forwarding_rule" "forwarding_rule" {
  name        = "forwarding-rule"
  provider    = google
  ip_protocol = "TCP"
  port_range  = "80"
  load_balancing_scheme = "EXTERNAL"
  target      = google_compute_target_http_proxy.http_target_proxy.id
  ip_address  = google_compute_global_address.lb_address.id
}
# HTTP Proxy
 resource "google_compute_target_http_proxy" "http_target_proxy" {
   name = "http-target-proxy"
   url_map = google_compute_url_map.url_map.id
 }

 resource "google_compute_url_map" "url_map" {
  name            = "url-map"
  default_service = google_compute_backend_service.backend.id
}

resource "google_compute_backend_service" "backend" {
  name = "backend-service"
  health_checks = [ google_compute_health_check.app_healthcheck.id ]
  backend {
    group = google_compute_instance_group_manager.app_managed_group.instance_group
    balancing_mode  = "UTILIZATION"
    capacity_scaler = 1.0
    max_utilization = 0.8
  }
}

# CLOUD SQL
resource "google_sql_database_instance" "sql_instance" {
    name = "sql-instance-petclinic"
    database_version = "MYSQL_8_0"
    region = var.google_region
    deletion_protection = false # in theory you should not do that, so it is for convinience for now | terraform option tbh, not GCP deletion protection
    depends_on = [ google_service_networking_connection.psa_connection ]
    root_password = "admin" # if in SCM then secret
    settings {
      deletion_protection_enabled = false
      tier = "db-custom-1-3840"
      location_preference {
        zone = var.google_zone
      }
      ip_configuration {
        ipv4_enabled = false
        private_network = google_compute_network.app_network.id
        enable_private_path_for_google_cloud_services = true
      }
    }
}

resource "google_sql_database" "petlinic_db" {
  name = "petclinic"
  instance = google_sql_database_instance.sql_instance.name
  charset = "UTF8"
  collation = "utf8_general_ci"
}

resource "google_sql_user" "petclinic_db_user" {
  name = "petclinic"
  instance = google_sql_database_instance.sql_instance.name
  password = "petclinic"
}
# INSTANCE
resource "google_compute_health_check" "app_healthcheck" {
  name = "app-health-check"
  check_interval_sec  = 5
  timeout_sec         = 5
  healthy_threshold   = 2
  unhealthy_threshold = 8 # 50 seconds

  tcp_health_check {
    port = "8080"
  }
}
# ADD AUTOSCALER
resource "google_compute_autoscaler" "autoscaler" {
  name = "autoscaler-petclinic-app"
  zone = var.google_zone
  target = google_compute_instance_group_manager.app_managed_group.id

  autoscaling_policy {
    max_replicas = var.app_max_replicas
    min_replicas = var.app_min_replicas
    cooldown_period = 60
  }
}

resource "google_compute_instance_template" "app_template" {
  name = "app-template"

  machine_type = "e2-small"
  tags = ["ssh-access", "java-8080-access"]
  disk {
    source_image = "cos-cloud/cos-stable" # for running docker images, jenkins-agent-sourcedisk should be good for jar file
    auto_delete = true
    boot = true
  }

  network_interface {
    network = google_compute_network.app_network.id
    subnetwork = google_compute_subnetwork.app_subnet_1.id
    access_config {
    }
  }
# TO BE CHANGED FOR PRODUCTION BUILD JAVA APP
  metadata_startup_script = <<EOF
#!/bin/bash

docker run -p 8080:8080 -e MYSQL_URL=${google_sql_database_instance.sql_instance.private_ip_address} \
  -e MYSQL_USER=${google_sql_user.petclinic_db_user.name} -e MYSQL_PASS=${google_sql_user.petclinic_db_user.password} \
  -e JAVA_OPTS='-Dspring-boot.run.profiles=mysql' \
  springcommunity/spring-framework-petclinic
  EOF

  service_account {
    email = var.service_account_email
    scopes = ["cloud-platform"] # scopes == all
  }
}


resource "google_compute_instance_group_manager" "app_managed_group" {
  name = "app-intance-group"
  base_instance_name = "app"
  zone = var.google_zone

  version {
    instance_template = google_compute_instance_template.app_template.self_link
  }

  named_port {
    name = "http"
    port = 8080
  }
  target_size = 2

  auto_healing_policies {
    health_check = google_compute_health_check.app_healthcheck.id
    initial_delay_sec = 3000
  }
}

output "LB_address" {
 value = google_compute_global_address.lb_address.address
}

output "DB_address" {
  value = google_sql_database_instance.sql_instance.private_ip_address
}
