resource "google_service_account" "feast_sa" {
  account_id   = var.name_prefix
  display_name = var.name_prefix
  project      = var.gcp_project_name
}

resource "google_service_account_key" "feast_sa" {
  service_account_id = google_service_account.feast_sa.name
}

resource "google_project_iam_member" "feast_dataproc_worker" {
  project = var.gcp_project_name
  role    = "roles/dataproc.worker"
  member  = "serviceAccount:${google_service_account.feast_sa.email}"
}

resource "google_project_iam_member" "feast_dataproc_editor" {
  project = var.gcp_project_name
  role    = "roles/dataproc.editor"
  member  = "serviceAccount:${google_service_account.feast_sa.email}"
}

resource "google_project_iam_member" "feast_batch_ingestion_storage" {
  project = var.gcp_project_name
  role    = "roles/storage.admin"
  member  = "serviceAccount:${google_service_account.feast_sa.email}"
}

resource "kubernetes_secret" "feast_sa_secret" {
  metadata {
    name = var.feast_sa_secret_name
  }
  data = {
    "credentials.json" = base64decode(google_service_account_key.feast_sa.private_key)
  }
}

