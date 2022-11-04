resource "google_project_service" "api_1_resource_manager" {
  service = "cloudresourcemanager.googleapis.com"
  disable_dependent_services = true
}

resource "google_project_service" "api_2_iam" {
  depends_on = [google_project_service.api_1_resource_manager]

  service = "iam.googleapis.com"
  disable_dependent_services = true
}

resource "google_project_service" "api_3_compute_engine" {
  depends_on = [google_project_service.api_2_iam]

  service = "compute.googleapis.com"
  disable_dependent_services = true
} 

resource "google_project_service" "api_4_cloud_os_login" {
  depends_on = [google_project_service.api_3_compute_engine]

  service = "oslogin.googleapis.com"
  disable_dependent_services = true
}

resource "google_project_service" "api_5_network_management" {
  depends_on = [google_project_service.api_4_cloud_os_login]

  service = "networkmanagement.googleapis.com"
  disable_dependent_services = true
} 

resource "google_project_service" "api_6_service_networking" {
  depends_on = [google_project_service.api_5_network_management]

  service = "servicenetworking.googleapis.com"
  disable_dependent_services = true
} 

resource "google_project_service" "api_7_cloudsql_admin" {
  depends_on = [google_project_service.api_6_service_networking]

  service = "sqladmin.googleapis.com"
  disable_dependent_services = true
} 

resource "google_project_service" "api_8_container" {
  depends_on = [google_project_service.api_7_cloudsql_admin]

  service = "container.googleapis.com"
  disable_dependent_services = true
} 

