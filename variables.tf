variable "google_project" {
  type = string
  default = "playground-s-11-5cd45b0d"
}

variable "service_account_email" {
  type = string
  default = "cli-service-account-1@playground-s-11-5cd45b0d.iam.gserviceaccount.com"
}

variable "google_region" {
  type = string
  default = "us-west3"
}

variable "google_zone" {
  type = string
  default = "us-west3-b"
}

variable "app_network_name" {
  type = string
  default = "app-network"
}

variable "app_subnet_name" {
  type = string
  default = "app-subnet"
}

variable "app_max_replicas" {
  type = number
  default = 1
}

variable "app_min_replicas" {
  type = number
  default = 1
}
