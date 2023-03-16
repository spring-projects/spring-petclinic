resource "aws_vpc" "myvpc" {
  cidr_block       = var.my_vpc
  instance_tenancy = "default"

  tags = {
    Name = var.myvpctag
  }
}

resource "aws_subnet" "my_subnet" {
  vpc_id     = aws_vpc.myvpc.id
  cidr_block = var.mypubsubnet
  availability_zone = "ap-south-1a"
  tags = {
    Name = var.publicsubnettag
  }
}

resource "aws_internet_gateway" "myintgw" {
  vpc_id = aws_vpc.myvpc.id

  tags = {
    Name = var.myintgwtag
  }
}

resource "aws_route_table" "mypubroute" {
  vpc_id = aws_vpc.myvpc.id

  route {
    cidr_block = var.mycidr_block
    gateway_id = aws_internet_gateway.myintgw.id
  }

  tags = {
    Name = "pubroutetag"
  }
}

resource "aws_route_table_association" "pubassociation" {
  subnet_id      = aws_subnet.my_subnet.id
  route_table_id = aws_route_table.mypubroute.id
}

resource "aws_network_interface" "mynetworkinterface" {
  subnet_id = aws_subnet.my_subnet.id

  tags = {
    Name = "public_network_interface"
  }
}







