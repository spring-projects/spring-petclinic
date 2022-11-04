# not by instance template, (or maybe by it), but certainly not a MIG but a standalone instance with only 22 port open

resource "google_compute_instance" "default" {
  name         = "internal-access-vm"
  machine_type = "e2-medium"
  zone         = var.zone

  tags = ["admin", "jenkins-vm", "ansible-vm"]

  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-11"
      labels = {
        my_label = "debian_boot"
      }
    }
  }


  network_interface {
    subnetwork = google_compute_subnetwork.terr_sub_vpc_1.id
    access_config {
    network_tier = "STANDARD"
    }
  }

  service_account {
    # Google recommends custom service accounts that have cloud-platform scope and permissions granted via IAM Roles.
    email = google_service_account.custom_service_account_1.email
    scopes = ["cloud-platform"]
  }
}