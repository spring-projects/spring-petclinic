resource "aws_subnet" "public_subnet_1_a" {
  vpc_id            = aws_vpc.petclinic_vpc.id
  availability_zone = var.availability_zone
  cidr_block        = lookup(var.cidr_block_public_subnet, terraform.workspace, "10.0.1.0/24") 

  tags = {
    Name       = "${terraform.workspace}-public_subnet_1_a"
    Created_by = "Terraform"
  }
}

resource "aws_subnet" "private_subnet_1_a" {
  vpc_id            = aws_vpc.petclinic_vpc.id
  availability_zone = var.availability_zone
  cidr_block        = lookup(var.cidr_block_private_subnet, terraform.workspace, "10.0.2.0/24")

  tags = {
    Name       = "${terraform.workspace}-private_subnet_1_a"
    Created_by = "Terraform"
  }
}