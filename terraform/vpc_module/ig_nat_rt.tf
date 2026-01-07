resource "aws_internet_gateway" "gw" {
  vpc_id = aws_vpc.petclinic_vpc.id

  tags = {
    Name       = "igw-petclinic"
    Created_by = "Terraform"
  }
}

resource "aws_eip" "nat_eip" {
  domain = "vpc"

  tags = {
    Name       = "nat-eip-petclinic"
    Created_by = "Terraform"
  }
}


resource "aws_nat_gateway" "nat" {
  allocation_id = aws_eip.nat_eip.id
  subnet_id     = aws_subnet.public_subnet_1_a.id

  tags = {
    Name = "${terraform.workspace}-public-nat-gateway-petclinic"
  }

  depends_on = [aws_eip.nat_eip]
}


resource "aws_route_table" "petclinic_public_rt" {
  vpc_id = aws_vpc.petclinic_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.gw.id
  }

  tags = {
    Name       = "${terraform.workspace}-petclinic_public_rt"
    Created_by = "Terraform"
  }
}

resource "aws_route_table" "petclinic_private_rt" {
  vpc_id = aws_vpc.petclinic_vpc.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.nat.id
  }

  tags = {
    Name       = "${terraform.workspace}-petclinic_private_rt"
    Created_by = "Terraform"
  }
}

resource "aws_route_table_association" "petclinic_public_rt_association" {
  subnet_id      = aws_subnet.public_subnet_1_a.id
  route_table_id = aws_route_table.petclinic_public_rt.id
}

resource "aws_route_table_association" "petclinic_private_rt_association" {
  subnet_id      = aws_subnet.private_subnet_1_a.id
  route_table_id = aws_route_table.petclinic_private_rt.id
}
