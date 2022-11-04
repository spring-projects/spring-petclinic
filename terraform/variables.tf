variable "gcp_project" {
    type        = string
    default     = "terraform-project-2-363117"
}
variable "region" {
    type        = string
    default     = "us-central1"
}
variable "zone" {
    type        = string
    default     = "us-central1-c"
}
variable "cred_file" {
    type        = string
    default     = "../keys/kuber-cicd-7a0d520fda36.json"   
}