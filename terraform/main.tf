terraform {
  required_providers {
    google = {
      source = "hashicorp/google"
      version = "4.36.0"
    }
    random = {
        source = "hashicorp/random"
    }
  }
}

provider "google" {
  # Configuration options
  credentials = file("${var.cred_file}")
  project = var.gcp_project
  region = var.region
  zone = var.zone
}