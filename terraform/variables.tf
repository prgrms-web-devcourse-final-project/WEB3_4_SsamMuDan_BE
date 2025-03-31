variable "aws_region" {
  description = "AWS Region"
  default     = "ap-northeast-2"
}

variable "aws_access_key" {}
variable "aws_secret_key" {}

variable "vpc_cidr" {
  description = "VPC CIDR Block"
  default     = "10.0.0.0/16"
}

variable "public_subnet_cidr" {
  description = "Public Subnet CIDR"
  default     = "10.0.1.0/24"
}

variable "private_subnet_cidr" {
  description = "Private Subnet CIDR"
  default     = "10.0.2.0/24"
}

variable "instance_type" {
  description = "EC2 Instance Type"
  default     = "t3.small"
}
