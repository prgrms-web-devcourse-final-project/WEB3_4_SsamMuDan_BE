INSERT INTO oauth_provider (name)
VALUES ('KAKAO'),
       ('NAVER'),
       ('GOOGLE');

INSERT INTO tech_stack (name, image_url)
VALUES ('Java', 'https://worldvectorlogo.com/download/java.svg'),
       ('Kotlin', 'https://worldvectorlogo.com/download/kotlin-1.svg'),
       ('Spring', 'https://worldvectorlogo.com/download/spring-3.svg'),
       ('Spring Boot', 'https://worldvectorlogo.com/download/spring-boot-1.svg'),
       ('JavaScript', 'https://worldvectorlogo.com/download/javascript-2.svg'),
       ('React', 'https://worldvectorlogo.com/download/react-2.svg'),
       ('Typescript', 'https://worldvectorlogo.com/download/typescript.svg'),
       ('Django', 'https://worldvectorlogo.com/download/django.svg'),
       ('MySQL', 'https://worldvectorlogo.com/download/mysql-3.svg'),
       ('MariaDB', 'https://worldvectorlogo.com/download/mariadb.svg'),
       ('PostgreSQL', 'https://worldvectorlogo.com/download/postgresql.svg'),
       ('MongoDB', 'https://worldvectorlogo.com/download/mongodb-icon-1.svg'),
       ('AWS', 'https://worldvectorlogo.com/download/aws-2.svg'),
       ('Node.js', 'https://worldvectorlogo.com/download/nodejs.svg');

INSERT INTO development_position (name)
VALUES ('프론트엔드'),
       ('백엔드'),
       ('풀스택');

INSERT INTO education_level (name)
VALUES ('입문'),
       ('초급'),
       ('중급');

INSERT INTO education_category (name)
VALUES ('백엔드'),
       ('프론트엔드'),
       ('웹 개발'),
       ('데이터'),
       ('프로그래밍 언어'),
       ('자료구조/알고리즘'),
       ('개발도구'),
       ('모바일');

INSERT INTO techEducation_type (name)
VALUES ('TechTube'),
       ('TechBook');

INSERT INTO order_category (name)
VALUES ('TechTube'),
       ('TechBook');

INSERT INTO community_category (name)
VALUES ('게시판'),
       ('코드 리뷰');