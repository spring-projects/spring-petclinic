terraform {
 backend "gcs" {
   bucket  = "bb08738a855c4a96-terraform-state-bucket"
   prefix  = "terraform/state"
 }
}
