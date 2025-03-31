output "web_server_public_ip" {
  description = "Public IP of the Web Server"
  value       = aws_instance.web_server.public_ip
}

output "db_server_private_ip" {
  description = "Private IP of the Database Server"
  value       = aws_instance.db_server.private_ip
}

output "vpc_id" {
  description = "VPC ID"
  value       = aws_vpc.main_vpc.id
}

output "public_subnet_id" {
  description = "Public Subnet ID"
  value       = aws_subnet.public_subnet.id
}

output "private_subnet_id" {
  description = "Private Subnet ID"
  value       = aws_subnet.private_subnet.id
}