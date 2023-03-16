resource "aws_instance" "jenkin" {
  ami                         = var.ami_id
  associate_public_ip_address = true
  instance_type               = var.instance_type
  key_name                    = data.aws_key_pair.mykey.key_name
  vpc_security_group_ids      = [aws_security_group.myrsgroup.id]
  subnet_id                   = aws_subnet.my_subnet.id
  availability_zone = "ap-south-1a"

  tags = {
    "Name" = "jenkins"
  }
}
   resource "null_resource" "jenkinnull" {
    triggers = {
   cluster_instance_ids = 1.2
  }
    connection {
    type     = "ssh"
    user     = "ubuntu"
    host        = aws_instance.jenkin.public_ip
    private_key = file("~/.ssh/id_rsa")
   }


provisioner "remote-exec" {
    
inline = [
"sudo apt-get update",
"sudo apt-get install openjdk-11-jdk -y",
"sudo apt-get install git -y",
"sudo apt-get install wget -y",
"sudo apt install curl -y",
"sleep 2m",
"curl -fsSL https://pkg.jenkins.io/debian-stable/jenkins.io.key | sudo tee /usr/share/keyrings/jenkins-keyring.asc > /dev/null",
"echo deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] https://pkg.jenkins.io/debian-stable binary/ | sudo tee /etc/apt/sources.list.d/jenkins.list > /dev/null",
"sudo apt install jenkins -y",
"sudo apt-get update",
  ]
  }
}



resource "aws_instance" "node1" {
  ami                         = var.ami_id
  associate_public_ip_address = true
  instance_type               = var.instance_type
  key_name                    = data.aws_key_pair.mykey.key_name
  vpc_security_group_ids      = [aws_security_group.myrsgroup.id]
  subnet_id                   = aws_subnet.my_subnet.id
  availability_zone           = "ap-south-1a"

  tags = {
    "Name" = "node1"
  }

}
resource "null_resource" "node1null" {
    triggers = {
   cluster_instance_ids = 1.2
  }
    connection {
    type     = "ssh"
    user     = "ubuntu"
    host        = aws_instance.node1.public_ip
    private_key = file("~/.ssh/id_rsa")
   }


provisioner "remote-exec" {
    
inline = [
"sudo apt-get update",
"sudo apt-get install openjdk-11-jdk -y",
"sudo apt-get install git -y",
"sudo apt-get install wget -y",
"sudo apt install curl -y",
"sudo apt install maven -y",
"sudo apt install software-properties-common -y",
"sudo add-apt-repository --yes --update ppa:ansible/ansible",
"sudo apt install ansible -y",
"sudo apt-get update",
  ]
  }
}





