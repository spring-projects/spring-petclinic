resource "aws_instance" "public_ec2_instance" {
  ami                         = var.ami
  instance_type               = lookup(var.instance_type, terraform.workspace)
  subnet_id                   = var.public_subnet_id
  key_name                    = aws_key_pair.ec2_keypair.key_name
  associate_public_ip_address = true
  vpc_security_group_ids = [
    aws_security_group.web_sg.id
  ]
  tags = {
    Name = "${terraform.workspace}-petclinic_ec2_instance"
  }
}