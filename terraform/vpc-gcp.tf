### VPC ###
resource "google_compute_network"  "terr_vpc_1" {
  depends_on = [google_project_service.api_7_cloudsql_admin]

    name = "terr-vpc-1"
    auto_create_subnetworks = false
    enable_ula_internal_ipv6 = true
}

resource "google_compute_subnetwork" "terr_sub_vpc_1" {
  name = "terr-sub-vpc-1"
  ip_cidr_range = var.ip_cidr_range
  network = google_compute_network.terr_vpc_1.id

  private_ip_google_access = true
  # private_ipv6_google_access = true
}
### VPC ###