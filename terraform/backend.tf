terraform {
  backend "gcs" {
    bucket  = "terraform-petclinic"
    prefix  = "terraform/state"
  }
}