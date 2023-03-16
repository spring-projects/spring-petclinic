data "aws_key_pair" "mykey" {
  filter {
    name   = "key-name"
    values = ["newkey"]
  }
}




