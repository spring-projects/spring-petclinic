variable "project_id" {
    type = string
    description = "Google Cloud Platform Project ID"
}

variable "app" {
    type = string
    description = "App Name"
}

variable "region" {
    type = string
    description = "Default region for the project"
}

variable "zone" {
    type = string
    description = "Default zone for the project"
}

variable "vm_name" {
    type = string
    description = "Name for Compute Engine in GCP"
}

variable "vm_type" {
    type = string
    description = "Type of Compute Engine in GCP"
}

variable "vm_image" {
    type = string
    description = "OS image of Compute Engine in GCP"
}

variable "db_name" {
    type = string
    description = "Name for CloudSQL instance in GCP"
}

variable "db_version" {
    type = string
    description = "Version of CloudSQL instance in GCP"
}

variable "db_tier" {
    type = string
    description = "Tier of CloudSQL in GCP"
}

variable "db_password" {
    type = string
    description = "Password for CloudSQL instance in GCP"
}