terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "6.27.0"
    }
  }
}

provider "aws" {
  region = "us-east-1"
}

resource "aws_vpc" "petclinic_vpc" {
  cidr_block = var.cidr_block
  tags = {
    Name       = "petclinic-vpc"
    Created_by = "Terraform"
  }
}

resource "aws_subnet" "public_subnet_1_a" {
  vpc_id            = aws_vpc.petclinic_vpc.id
  availability_zone = var.availability_zone
  cidr_block        = var.cidr_block_public_subnet

  tags = {
    Name       = "public_subnet_1_a"
    Created_by = "Terraform"
  }
}

resource "aws_subnet" "private_subnet_1_a" {
  vpc_id            = aws_vpc.petclinic_vpc.id
  availability_zone = var.availability_zone
  cidr_block        = var.cidr_block_private_subnet

  tags = {
    Name       = "private_subnet_1_a"
    Created_by = "Terraform"
  }
}

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
    Name = "public-nat-gateway-petclinic"
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
    Name       = "petclinic_public_rt"
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
    Name       = "petclinic_private_rt"
    Created_by = "Terraform"
  }
}

resource "aws_security_group" "web_sg" {
  name        = "web-sg"
  description = "Allow HTTP and SSH"
  vpc_id      = aws_vpc.petclinic_vpc.id

  ingress {
    description = "HTTP"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

    ingress {
    description = "HTTP"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
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

resource "tls_private_key" "ec2_key" {
  algorithm = "RSA"
  rsa_bits  = 4096
}

resource "local_file" "pem_file" {
  filename        = "${path.module}/petclinic-key.pem"
  content         = tls_private_key.ec2_key.private_key_pem
  file_permission = "0400"
}

resource "aws_key_pair" "ec2_keypair" {
  key_name   = "petclinic-key"
  public_key = tls_private_key.ec2_key.public_key_openssh
}


resource "aws_instance" "public_ec2_instance" {
  ami           = "ami-0ecb62995f68bb549"
  instance_type = "t2.medium"
  subnet_id     = aws_subnet.public_subnet_1_a.id
  key_name = aws_key_pair.ec2_keypair.key_name
  associate_public_ip_address = true
  vpc_security_group_ids = [
    aws_security_group.web_sg.id
  ]
  tags = {
    Name = "public_ec2_instance"
  }
}