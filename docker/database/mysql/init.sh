# MySQL 서버가 준비될 때까지 대기
echo "$(date '+%Y-%m-%d %H:%M:%S') - MySQL 서버 준비 중, 대기 시간: 5초"
sleep 5

mysql -u root -p${MYSQL_ROOT_PASSWORD} <<EOF
CREATE DATABASE IF NOT EXISTS cotree;
USE cotree;

# Exporter 사용자 생성
CREATE USER IF NOT EXISTS '${EXPORTER_NAME}'@'%' IDENTIFIED BY '${EXPORTER_PASSWORD}' WITH MAX_USER_CONNECTIONS 3;
GRANT PROCESS, REPLICATION CLIENT ON *.* TO '${EXPORTER_NAME}'@'%';
FLUSH PRIVILEGES;
EOF

# 로그: 쿼리 실행 후
echo "$(date '+%Y-%m-%d %H:%M:%S') - cotree 데이터베이스와 exporter 사용자 생성 완료"

# 스크립트 종료 로그
echo "$(date '+%Y-%m-%d %H:%M:%S') - MySQL 초기화 완료"
