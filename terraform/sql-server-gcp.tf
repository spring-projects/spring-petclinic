resource "google_compute_global_address" "private_ip_address" {
  depends_on = [google_project_service.api_7_cloudsql_admin]

  # address       = var.sql_private_ip
  name          = "terr-sql-private-ip-address"
  purpose       = "VPC_PEERING"
  address_type  = "INTERNAL"
  prefix_length = 16
  network       = google_compute_network.terr_vpc_1.id
}
resource "google_service_networking_connection" "private_vpc_connection" {
  depends_on = [google_project_service.api_7_cloudsql_admin]

  network                 = google_compute_network.terr_vpc_1.id
  service                 = "servicenetworking.googleapis.com"
  reserved_peering_ranges = [google_compute_global_address.private_ip_address.name]
}
resource "random_id" "db_name_suffix" {
  byte_length = 3
}
resource "google_sql_database_instance" "terr_sql_private_instance" {
  name             = "sql-private-instance-${random_id.db_name_suffix.hex}"
  region           = var.region
  database_version = "MYSQL_8_0"
  depends_on = [google_service_networking_connection.private_vpc_connection]
  deletion_protection = false
  
  settings {
    tier = "db-g1-small"
    disk_size         = 10
    ip_configuration {
      require_ssl = false
      ipv4_enabled    = false
      private_network = google_compute_network.terr_vpc_1.id
    }
  }
}

# Database and user will be created  with sql file, so this is likely not needed.
# Moreover, terraform should not do instance configuration, that's a job for Ansible
#
#
# resource "google_sql_database" "database" {
#   depends_on = [google_project_service.api_2_cloudsql_admin]

#   name = var.sql_php_app_database_name
#   instance = google_sql_database_instance.terr_sql_private_instance.name
# }
# resource "google_sql_user" "db_user_1" {
#   name     = var.sql_user_1_name
#   instance = google_sql_database_instance.terr_sql_private_instance.name
#   password = var.sql_user_1_password
# }