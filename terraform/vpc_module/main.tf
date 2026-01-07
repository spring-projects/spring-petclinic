resource "aws_vpc" "petclinic_vpc" {
  cidr_block = var.cidr_block
  tags = {
    Name       = "petclinic-vpc"
    Created_by = "Terraform"
  }
}