# VPC 생성
resource "aws_vpc" "main_vpc" {
  cidr_block           = var.vpc_cidr
  enable_dns_support   = true
  enable_dns_hostnames = true

  tags = {
    Name = "Team15-VPC"
    Team = "devcos4-team15"
  }
}

# 서브넷 생성 (퍼블릭)
resource "aws_subnet" "public_subnet" {
  vpc_id                  = aws_vpc.main_vpc.id
  cidr_block              = var.public_subnet_cidr
  map_public_ip_on_launch = true

  tags = {
    Name = "Team15-public-subnet"
    Team = "devcos4-team15"
  }
}

# 서브넷 생성 (프라이빗)
resource "aws_subnet" "private_subnet" {
  vpc_id     = aws_vpc.main_vpc.id
  cidr_block = var.private_subnet_cidr

  tags = {
    Name = "Team15-private-subnet"
    Team = "devcos4-team15"
  }
}

# 인터넷 게이트웨이
resource "aws_internet_gateway" "gw" {
  vpc_id = aws_vpc.main_vpc.id

  tags = {
    Name = "Team15-IGW"
    Team = "devcos4-team15"
  }
}

# 라우트 테이블 설정
resource "aws_route_table" "public_rt" {
  vpc_id = aws_vpc.main_vpc.id
}

resource "aws_route" "public_internet_access" {
  route_table_id         = aws_route_table.public_rt.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.gw.id
}

resource "aws_route_table_association" "public_assoc" {
  subnet_id      = aws_subnet.public_subnet.id
  route_table_id = aws_route_table.public_rt.id
}

# 보안 그룹 (EC2-1: NginX + Spring Boot)
resource "aws_security_group" "web_sg" {
  name   = "team15-api-server-sg"
  vpc_id = aws_vpc.main_vpc.id

  ingress {
    from_port = 22
    to_port   = 22
    protocol  = "tcp"
    cidr_blocks = ["0.0.0.0/0"]  # SSH 허용
  }

  ingress {
    from_port = 80
    to_port   = 80
    protocol  = "tcp"
    cidr_blocks = ["0.0.0.0/0"]  # HTTP 허용
  }

  ingress {
    from_port = 443
    to_port   = 443
    protocol  = "tcp"
    cidr_blocks = ["0.0.0.0/0"]  # HTTPS 허용
  }
}

# 보안 그룹 (EC2-2: MySQL 서버)
resource "aws_security_group" "db_sg" {
  name   = "team15-db-sg"
  vpc_id = aws_vpc.main_vpc.id

  ingress {
    from_port = 3306
    to_port   = 3306
    protocol  = "tcp"
    security_groups = [aws_security_group.web_sg.id]  # EC2-1에서만 접근 가능
  }
}

# EC2-1 (Web Server)
resource "aws_instance" "web_server" {
  ami           = "ami-0d5bb3742db8fc264"
  instance_type = var.instance_type
  subnet_id     = aws_subnet.public_subnet.id
  security_groups = [aws_security_group.web_sg.id]
  key_name      = "team15-api-server-key"

  root_block_device {
    volume_size           = 30
    volume_type           = "gp3"
    delete_on_termination = true
  }
  user_data = <<-EOF
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

# nginx 설치
docker run -d \
  --name npm_1 \
  --restart unless-stopped \
  -p 80:80 \
  -p 443:443 \
  -p 81:81 \
  -e TZ=Asia/Seoul \
  -v /dockerProjects/npm_1/volumes/data:/data \
  -v /dockerProjects/npm_1/volumes/etc/letsencrypt:/etc/letsencrypt \
  jc21/nginx-proxy-manager:latest
EOF

  tags = {
    Name = "Team15-api-server"
    Team = "devcos4-team15"
  }
}

# EC2-2 (MySQL Server)
resource "aws_instance" "db_server" {
  ami           = "ami-0d5bb3742db8fc264"
  instance_type = var.instance_type
  subnet_id     = aws_subnet.private_subnet.id
  security_groups = [aws_security_group.db_sg.id]
  key_name      = "team15-db-server-key"

  user_data = <<-EOF
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

# mysql 설치
docker run -d \
  --name mysql_1 \
  --restart unless-stopped \
  -v /dockerProjects/mysql_1/volumes/var/lib/mysql:/var/lib/mysql \
  -v /dockerProjects/mysql_1/volumes/etc/mysql/conf.d:/etc/mysql/conf.d \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=1234 \
  -e TZ=Asia/Seoul \
  mysql:latest

# MySQL 컨테이너가 준비될 때까지 대기
echo "MySQL이 기동될 때까지 대기 중..."
until docker exec mysql_1 mysql -uroot -p1234 -e "SELECT 1" &> /dev/null; do
  echo "MySQL이 아직 준비되지 않음. 5초 후 재시도..."
  sleep 5
done
echo "MySQL이 준비됨. 초기화 스크립트 실행 중..."

docker exec mysql_1 mysql -uroot -p1234 -e "
CREATE USER 'team15local'@'127.0.0.1' IDENTIFIED WITH caching_sha2_password BY '1234';
CREATE USER 'team15local'@'${aws_instance.web_server.private_ip}' IDENTIFIED WITH caching_sha2_password BY '1234';
CREATE USER 'team15'@'%' IDENTIFIED WITH caching_sha2_password BY '1234';

GRANT ALL PRIVILEGES ON *.* TO 'team15local'@'127.0.0.1';
GRANT ALL PRIVILEGES ON *.* TO 'team15local'@'${aws_instance.web_server.private_ip}';

CREATE DATABASE glog_prod;

FLUSH PRIVILEGES;
"

EOF

  root_block_device {
    volume_size           = 30
    volume_type           = "gp3"
    delete_on_termination = true
  }

  tags = {
    Name = "Team15-db-server"
    Team = "devcos4-team15"
  }
}

# 탄력적 IP (Web Server)
resource "aws_eip" "web_server_eip" {
  instance = aws_instance.web_server.id

  tags = {
    Name = "Team15-api-server-eip"
    Team = "devcos4-team15"
  }
}

# EIP와 EC2 인스턴스를 연결 (자동 재연결)
resource "aws_eip_association" "web_server_eip_assoc" {
  instance_id   = aws_instance.web_server.id
  allocation_id = aws_eip.web_server_eip.id
}