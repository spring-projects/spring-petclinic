resource "tls_private_key" "ec2_key" {
  algorithm = "RSA"
  rsa_bits  = 4096
}

resource "local_file" "pem_file" {
  filename        = "${path.module}/${terraform.workspace}-petclinic-key.pem"
  content         = tls_private_key.ec2_key.private_key_pem
  file_permission = "0400"
}

resource "aws_key_pair" "ec2_keypair" {
  key_name   = "${terraform.workspace}-petclinic-key"
  public_key = tls_private_key.ec2_key.public_key_openssh
}