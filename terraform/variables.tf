variable "gcp_project" {
    type        = string
    default     = "kuber-cicd"
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
variable "ip_cidr_range" {
    type        = string
    default     = "10.10.0.0/16"  
}