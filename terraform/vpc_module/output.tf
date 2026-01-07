output "vpc_id" {
  value = aws_vpc.petclinic_vpc.id
}

output "public_subnet_ids" {
  value = aws_subnet.public_subnet_1_a.id
}
