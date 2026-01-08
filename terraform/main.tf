terraform {
  required_version = ">= 1.6.0"
  
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "~> 5.0"
    }
  }

  # backend "gcs" {
  #   bucket = "terraform-state-bucket"
  #   prefix = "terraform/state"
  # }
}

provider "google" {
  project = var.project_id
  region  = var.region
}

locals {
  service_name = "petclinic-${var.environment}"
  labels = {
    environment = var.environment
    application = "petclinic"
    managed-by  = "terraform"
  }
}

# Artifact Registry Repository
resource "google_artifact_registry_repository" "petclinic" {
  location      = var.region
  repository_id = "petclinic"
  description   = "Docker repository for PetClinic application"
  format        = "DOCKER"
  
  labels = local.labels
}

# Service Account for Cloud Run
resource "google_service_account" "petclinic" {
  account_id   = "petclinic-${var.environment}"
  display_name = "PetClinic Service Account (${var.environment})"
  description  = "Service account for PetClinic Cloud Run service"
}

# Grant Cloud Run service account 
resource "google_project_iam_member" "petclinic_log_writer" {
  project = var.project_id
  role    = "roles/logging.logWriter"
  member  = "serviceAccount:${google_service_account.petclinic.email}"
}

resource "google_project_iam_member" "petclinic_metric_writer" {
  project = var.project_id
  role    = "roles/monitoring.metricWriter"
  member  = "serviceAccount:${google_service_account.petclinic.email}"
}

# Cloud Run Service
resource "google_cloud_run_v2_service" "petclinic" {
  name     = local.service_name
  location = var.region
  
  labels = local.labels
  
  template {
    service_account = google_service_account.petclinic.email
    
    scaling {
      min_instance_count = var.min_instances
      max_instance_count = var.max_instances
    }
    
    containers {
      image = "${var.region}-docker.pkg.dev/${var.project_id}/petclinic/spring-petclinic:${var.image_tag}"
      
      resources {
        limits = {
          cpu    = var.cpu_limit
          memory = var.memory_limit
        }
        cpu_idle          = true
        startup_cpu_boost = true
      }
      
      env {
        name  = "SPRING_PROFILES_ACTIVE"
        value = var.environment
      }
      
      env {
        name  = "JAVA_OPTS"
        value = "-XX:MaxRAMPercentage=75.0 -XX:+UseContainerSupport"
      }
      
      # Health check probes
      startup_probe {
        http_get {
          path = "/actuator/health/liveness"
          port = 8080
        }
        initial_delay_seconds = 0
        timeout_seconds       = 1
        period_seconds        = 3
        failure_threshold     = 10
      }
      
      liveness_probe {
        http_get {
          path = "/actuator/health/liveness"
          port = 8080
        }
        initial_delay_seconds = 0
        timeout_seconds       = 1
        period_seconds        = 10
        failure_threshold     = 3
      }
    }
  }
  
  traffic {
    type    = "TRAFFIC_TARGET_ALLOCATION_TYPE_LATEST"
    percent = 100
  }
  
  depends_on = [
    google_project_iam_member.petclinic_log_writer,
    google_project_iam_member.petclinic_metric_writer,
  ]
}

# IAM Policy for Public Access
resource "google_cloud_run_v2_service_iam_member" "public_access" {
  count = var.allow_public_access ? 1 : 0
  
  project  = var.project_id
  location = var.region
  name     = google_cloud_run_v2_service.petclinic.name
  role     = "roles/run.invoker"
  member   = "allUsers"
}