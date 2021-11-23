output "load_balancer_ip" {
  value = aws_lb.default.dns_name
}