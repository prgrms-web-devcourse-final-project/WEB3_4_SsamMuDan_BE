terraform {
  // aws 라이브러리 불러옴
  required_providers {
    aws = {
      source = "hashicorp/aws"
    }
  }
}

# AWS 설정 시작
provider "aws" {
  region = var.region
}
# AWS 설정 끝

# VPC 설정 시작
resource "aws_vpc" "vpc_1" {
  cidr_block = "10.0.0.0/16"

  enable_dns_support   = true
  enable_dns_hostnames = true

  tags = {
    Name = "${var.prefix}-vpc-1"
    Team = var.team_tag
  }
}

resource "aws_subnet" "subnet_1" {
  vpc_id                  = aws_vpc.vpc_1.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "${var.region}a"
  map_public_ip_on_launch = true

  tags = {
    Name = "${var.prefix}-subnet-1"
    Team = var.team_tag
  }
}

resource "aws_subnet" "subnet_2" {
  vpc_id                  = aws_vpc.vpc_1.id
  cidr_block              = "10.0.2.0/24"
  availability_zone       = "${var.region}b"
  map_public_ip_on_launch = true

  tags = {
    Name = "${var.prefix}-subnet-2"
    Team = var.team_tag
  }
}

resource "aws_subnet" "subnet_3" {
  vpc_id                  = aws_vpc.vpc_1.id
  cidr_block              = "10.0.3.0/24"
  availability_zone       = "${var.region}c"
  map_public_ip_on_launch = true

  tags = {
    Name = "${var.prefix}-subnet-3"
    Team = var.team_tag
  }
}

resource "aws_subnet" "subnet_4" {
  vpc_id                  = aws_vpc.vpc_1.id
  cidr_block              = "10.0.4.0/24"
  availability_zone       = "${var.region}d"
  map_public_ip_on_launch = true

  tags = {
    Name = "${var.prefix}-subnet-4"
    Team = var.team_tag
  }
}

resource "aws_internet_gateway" "igw_1" {
  vpc_id = aws_vpc.vpc_1.id

  tags = {
    Name = "${var.prefix}-igw-1"
    Team = var.team_tag
  }
}

resource "aws_route_table" "rt_1" {
  vpc_id = aws_vpc.vpc_1.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw_1.id
  }

  tags = {
    Name = "${var.prefix}-rt-1"
    Team = var.team_tag
  }
}

resource "aws_route_table_association" "association_1" {
  subnet_id      = aws_subnet.subnet_1.id
  route_table_id = aws_route_table.rt_1.id
}

resource "aws_route_table_association" "association_2" {
  subnet_id      = aws_subnet.subnet_2.id
  route_table_id = aws_route_table.rt_1.id
}

resource "aws_route_table_association" "association_3" {
  subnet_id      = aws_subnet.subnet_3.id
  route_table_id = aws_route_table.rt_1.id
}

resource "aws_route_table_association" "association_4" {
  subnet_id      = aws_subnet.subnet_4.id
  route_table_id = aws_route_table.rt_1.id
}

resource "aws_security_group" "sg_1" {
  name = "${var.prefix}-sg-1"

  ingress {
    from_port = 0
    to_port   = 0
    protocol  = "all"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port = 0
    to_port   = 0
    protocol  = "all"
    cidr_blocks = ["0.0.0.0/0"]
  }

  vpc_id = aws_vpc.vpc_1.id

  tags = {
    Name = "${var.prefix}-sg-1"
    Team = var.team_tag
  }
}

# EC2 설정 시작

# EC2 역할 생성
resource "aws_iam_role" "ec2_role_1" {
  name = "${var.prefix}-ec2-role-1"

  # 이 역할에 대한 신뢰 정책 설정. EC2 서비스가 이 역할을 가정할 수 있도록 설정
  assume_role_policy = <<EOF
  {
    "Version": "2012-10-17",
    "Statement": [
      {
        "Sid": "",
        "Action": "sts:AssumeRole",
        "Principal": {
            "Service": "ec2.amazonaws.com"
        },
        "Effect": "Allow"
      }
    ]
  }
  EOF

  tags = {
    Name = "${var.prefix}-ec2-role-1"
    Team = var.team_tag
  }
}

# EC2 역할에 AmazonS3FullAccess 정책을 부착
resource "aws_iam_role_policy_attachment" "s3_full_access" {
  role       = aws_iam_role.ec2_role_1.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonS3FullAccess"
}

# EC2 역할에 AmazonEC2RoleforSSM 정책을 부착
resource "aws_iam_role_policy_attachment" "ec2_ssm" {
  role       = aws_iam_role.ec2_role_1.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonEC2RoleforSSM"
}

# IAM 인스턴스 프로파일 생성
resource "aws_iam_instance_profile" "instance_profile_1" {
  name = "${var.prefix}-instance-profile-1"
  role = aws_iam_role.ec2_role_1.name

  tags = {
    Name = "${var.prefix}-instance-profile-1"
    Team = var.team_tag
  }
}

locals {
  ec2_user_data_base = <<-END_OF_FILE
#!/bin/bash
# 가상 메모리 4GB 설정
sudo dd if=/dev/zero of=/swapfile bs=128M count=32
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
sudo sh -c 'echo "/swapfile swap swap defaults 0 0" >> /etc/fstab'

# 도커 설치 및 실행/활성화
yum install docker -y
systemctl enable docker
systemctl start docker

# 도커 네트워크 생성
docker network create common

# nginx 설치
docker run -d \
  --name npm_1 \
  --restart unless-stopped \
  --network common \
  -p 80:80 \
  -p 443:443 \
  -p 81:81 \
  -e TZ=Asia/Seoul \
  -v /dockerProjects/npm_1/volumes/data:/data \
  -v /dockerProjects/npm_1/volumes/etc/letsencrypt:/etc/letsencrypt \
  jc21/nginx-proxy-manager:latest

# ha proxy 설치
## 설정파일을 위한 디렉토리 생성
mkdir -p /dockerProjects/ha_proxy_1/volumes/usr/local/etc/haproxy/lua

cat << 'EOF' > /dockerProjects/ha_proxy_1/volumes/usr/local/etc/haproxy/lua/retry_on_502_504.lua
core.register_action("retry_on_502_504", { "http-res" }, function(txn)
  local status = txn.sf:status()
  if status == 502 or status == 504 then
    txn:Done()
  end
end)
EOF

## 설정파일 생성
echo -e "
global
    lua-load /usr/local/etc/haproxy/lua/retry_on_502_504.lua

resolvers docker
    nameserver dns1 127.0.0.11:53
    resolve_retries       3
    timeout retry         1s
    hold valid            10s

defaults
    mode http
    timeout connect 5s
    timeout client 60s
    timeout server 60s

frontend http_front
    bind *:80
    acl host_app1 hdr_beg(host) -i ${var.app_1_domain}

    use_backend http_back_1 if host_app1

backend http_back_1
    balance roundrobin
    option httpchk GET /actuator/health
    default-server inter 2s rise 1 fall 1 init-addr last,libc,none resolvers docker
    option redispatch
    http-response lua.retry_on_502_504

    server app_server_1_1 app1_1:8080 check
    server app_server_1_2 app1_2:8080 check
" > /dockerProjects/ha_proxy_1/volumes/usr/local/etc/haproxy/haproxy.cfg

docker run \
  -d \
  --network common \
  -p 8090:80 \
  -v /dockerProjects/ha_proxy_1/volumes/usr/local/etc/haproxy:/usr/local/etc/haproxy \
  -e TZ=Asia/Seoul \
  --name ha_proxy_1 \
  haproxy

# redis 설치
docker run -d \
  --name=redis_1 \
  --restart unless-stopped \
  --network common \
  -p 6379:6379 \
  -e TZ=Asia/Seoul \
  redis --requirepass ${var.password_1}

# mysql 설치
docker run -d \
  --name mysql_1 \
  --restart unless-stopped \
  -v /dockerProjects/mysql_1/volumes/var/lib/mysql:/var/lib/mysql \
  -v /dockerProjects/mysql_1/volumes/etc/mysql/conf.d:/etc/mysql/conf.d \
  --network common \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=${var.password_1} \
  -e TZ=Asia/Seoul \
  mysql:latest

# MySQL 컨테이너가 준비될 때까지 대기
echo "MySQL이 기동될 때까지 대기 중..."
until docker exec mysql_1 mysql -uroot -p${var.password_1} -e "SELECT 1" &> /dev/null; do
  echo "MySQL이 아직 준비되지 않음. 5초 후 재시도..."
  sleep 5
done
echo "MySQL이 준비됨. 초기화 스크립트 실행 중..."

docker exec mysql_1 mysql -uroot -p${var.password_1} -e "
CREATE USER 'lldjlocal'@'127.0.0.1' IDENTIFIED WITH caching_sha2_password BY '1234';
CREATE USER 'lldjlocal'@'172.18.%.%' IDENTIFIED WITH caching_sha2_password BY '1234';
CREATE USER 'lldj'@'%' IDENTIFIED WITH caching_sha2_password BY '${var.password_1}';

GRANT ALL PRIVILEGES ON *.* TO 'lldjlocal'@'127.0.0.1';
GRANT ALL PRIVILEGES ON *.* TO 'lldjlocal'@'172.18.%.%';
GRANT ALL PRIVILEGES ON *.* TO 'lldj'@'%';

CREATE DATABASE dementor_prod;

FLUSH PRIVILEGES;
"

echo "${var.github_access_token_1}" | docker login ghcr.io -u ${var.github_access_token_1_owner} --password-stdin

END_OF_FILE
}


# EC2 인스턴스 생성
resource "aws_instance" "ec2_1" {
  # 사용할 AMI ID
  ami = "ami-070e986143a3041b6"
  # EC2 인스턴스 유형
  instance_type = "t3.micro"
  # 사용할 서브넷 ID
  subnet_id = aws_subnet.subnet_2.id
  # 적용할 보안 그룹 ID
  vpc_security_group_ids = [aws_security_group.sg_1.id]
  # 퍼블릭 IP 연결 설정
  associate_public_ip_address = true

  # 인스턴스에 IAM 역할 연결
  iam_instance_profile = aws_iam_instance_profile.instance_profile_1.name

  # 인스턴스에 태그 설정
  tags = {
    Name = "${var.prefix}-ec2-1"
    Team = var.team_tag
  }

  # 루트 볼륨 설정
  root_block_device {
    volume_type = "gp3"
    volume_size = 30 # 볼륨 크기를 12GB로 설정
  }

  user_data = <<-EOF
${local.ec2_user_data_base}
EOF
}
# EC2 for DB (MySQL + Redis)

resource "aws_security_group" "sg_db" {
  name   = "${var.prefix}-sg-db"
  vpc_id = aws_vpc.vpc_1.id

  ingress {
    description = "MySQL"
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Redis"
    from_port   = 6379
    to_port     = 6379
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "${var.prefix}-sg-db"
    Team = var.team_tag
  }
}

resource "aws_instance" "ec2_db" {
  ami                         = "ami-070e986143a3041b6"
  instance_type               = "t3.micro"
  subnet_id                   = aws_subnet.subnet_3.id
  vpc_security_group_ids = [aws_security_group.sg_db.id]
  associate_public_ip_address = true

  iam_instance_profile = aws_iam_instance_profile.instance_profile_1.name

  tags = {
    Name = "${var.prefix}-ec2-db"
    Team = var.team_tag
  }

  root_block_device {
    volume_type = "gp3"
    volume_size = 30
  }

  user_data = <<-EOF
#!/bin/bash
# 가상 메모리 4GB 설정
sudo dd if=/dev/zero of=/swapfile bs=128M count=32
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
echo "/swapfile swap swap defaults 0 0" | sudo tee -a /etc/fstab

yum install -y docker git
systemctl enable docker
systemctl start docker

docker network create common-network

curl -L "https://github.com/docker/compose/releases/download/v2.24.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

cd /root
git clone https://github.com/prgrms-web-devcourse-final-project/WEB3_4_SsamMuDan_BE.git
cd WEB3_4_SsamMuDan_BE/docker/database

cat > .env <<ENVVARS
MYSQL_ROOT_PASSWORD=${var.db_password}
MYSQL_OUT_PORT=3306
MYSQL_IN_PORT=3306
MYSQL_TIME_ZONE=Asia/Seoul
MYSQL_QUERY_LOG_PATH=/var/log/mysql

EXPORTER_NAME=${var.exporter}
EXPORTER_PASSWORD=${var.exporter_password}

REDIS_PASSWORD=${var.db_password}
REDIS_OUT_PORT=6379
REDIS_IN_PORT=6379
ENVVARS

docker-compose -f db-docker-compose.yml up -d
EOF
}

resource "aws_security_group" "sg_monitoring" {
  name   = "${var.prefix}-sg-monitoring"
  vpc_id = aws_vpc.vpc_1.id

  ingress {
    description = "Grafana Web"
    from_port   = 3000
    to_port     = 3000
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "${var.prefix}-sg-monitoring"
    Team = var.team_tag
  }
}

resource "aws_instance" "ec2_monitoring" {
  ami                         = "ami-070e986143a3041b6"
  instance_type               = "t3.micro"
  subnet_id                   = aws_subnet.subnet_4.id
  vpc_security_group_ids = [aws_security_group.sg_monitoring.id]
  associate_public_ip_address = true

  iam_instance_profile = aws_iam_instance_profile.instance_profile_1.name

  tags = {
    Name = "${var.prefix}-ec2-monitoring"
    Team = var.team_tag
  }

  root_block_device {
    volume_type = "gp3"
    volume_size = 20
  }

  user_data = <<-EOF
#!/bin/bash
# 가상 메모리 4GB 설정
sudo dd if=/dev/zero of=/swapfile bs=128M count=32
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
echo "/swapfile swap swap defaults 0 0" | sudo tee -a /etc/fstab

yum install -y docker
systemctl enable docker
systemctl start docker

docker network create common


curl -L "https://github.com/docker/compose/releases/download/v2.24.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

cd /root
git clone https://github.com/prgrms-web-devcourse-final-project/WEB3_4_SsamMuDan_BE.git
cd WEB3_4_SsamMuDan_BE/docker/monitoring

cat > .env <<ENVVARS
PROMETHEUS_OUT_PORT=9090
GRAFANA_OUT_PORT=3000
ENVVARS

docker-compose -f monitoring-docker-compose.yml up -d

EOF
}

# Elastic IP 리소스 생성
resource "aws_eip" "eip_1" {
  instance = aws_instance.ec2_1.id

  tags = {
    Name = "${var.prefix}-eip-1"
    Team = var.team_tag
  }
}
