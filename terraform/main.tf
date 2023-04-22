terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "4.62.0"
    }
  }

  backend "gcs" {}
}

#TODO parameterize gcp project ID in terraform plan/apply in CI

module "gke_auth" {
  source       = "terraform-google-modules/kubernetes-engine/google//modules/auth"
  version      = "25.0.0"
  depends_on   = [module.gke]
  project_id   = var.project_id
  location     = module.gke.location
  cluster_name = module.gke.name
}

resource "local_file" "kubeconfig" {
  content  = module.gke_auth.kubeconfig_raw
  filename = "kubeconfig-${var.env_name}"
}

module "gcp-network" {
  source       = "terraform-google-modules/network/google"
  version      = "7.0.0"
  project_id   = var.project_id
  network_name = "${var.network}-${var.env_name}"

  subnets = [
    {
      subnet_name   = "${var.subnetwork}-${var.env_name}"
      subnet_ip     = "10.10.0.0/16"
      subnet_region = var.region
    },
  ]

  secondary_ranges = {
    "${var.subnetwork}-${var.env_name}" = [
      {
        range_name    = var.ip_range_pods_name
        ip_cidr_range = "10.20.0.0/16"
      },
      {
        range_name    = var.ip_range_services_name
        ip_cidr_range = "10.30.0.0/16"
      },
    ]
  }
}


module "gke" {
  source            = "terraform-google-modules/kubernetes-engine/google//modules/private-cluster"
  version           = "25.0.0"
  project_id        = var.project_id
  name              = "${var.cluster_name}-${var.env_name}"
  regional          = true
  region            = var.region
  network           = module.gcp-network.network_name
  subnetwork        = module.gcp-network.subnets_names[0]
  ip_range_pods     = var.ip_range_pods_name
  ip_range_services = var.ip_range_services_name

  node_pools = [
    {
      name           = "node-pool"
      machine_type   = "e2-medium"
      node_locations = "europe-west1-b,europe-west1-c,europe-west1-d"
      min_count      = 1
      max_count      = 2
      disk_size_gb   = 30
    },
  ]
}
