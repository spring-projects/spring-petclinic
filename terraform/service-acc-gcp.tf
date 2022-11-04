# 3 - Create service account
resource "google_service_account" "custom_service_account_1" {
  depends_on = [google_project_service.api_7_cloudsql_admin]

  account_id   = "custom-service-account-1"
  display_name = "Custom service account"
  description = "Service Account for MIG and Databases"
}

#give instance Admin v1
resource "google_project_iam_member" "service_account_1_IAv1" {
  depends_on = [google_project_service.api_7_cloudsql_admin]
  
  project = var.gcp_project
  role    = "roles/compute.instanceAdmin.v1"
  member  = "serviceAccount:${google_service_account.custom_service_account_1.email}"
}

# NOT NEEEDED, DELETE IF WORKS WITHOUT IT
 #give storage object Admin
# resource "google_project_iam_member" "service_account_1_OA" {
#   depends_on = [google_project_service.api_7_iam]
  
#   project = var.gcp_project
#   role    = "roles/storage.objectAdmin"
#   member  = "serviceAccount:${google_service_account.custom_service_account_1.email}"
# }

#give network Admin
resource "google_project_iam_member" "service_account_1_NA" {
  depends_on = [google_project_service.api_7_cloudsql_admin]
  
  project = var.gcp_project
  role    = "roles/compute.networkAdmin"
  member  = "serviceAccount:${google_service_account.custom_service_account_1.email}"
}

#give access to IAP
resource "google_project_iam_member" "service_account_1_IAP" {
  depends_on = [google_project_service.api_7_cloudsql_admin]
  
  project = var.gcp_project
  role    = "roles/iap.tunnelResourceAccessor"
  member  = "serviceAccount:${google_service_account.custom_service_account_1.email}"
}

# NOT NEEEDED, DELETE IF WORKS WITHOUT IT
# resource "google_project_iam_member" "service_account_1_SA" {
#   depends_on = [google_project_service.api_7_iam]
  
#   project = var.gcp_project
#   role    = "roles/storage.admin"
#   member  = "serviceAccount:${google_service_account.custom_service_account_1.email}"
# }