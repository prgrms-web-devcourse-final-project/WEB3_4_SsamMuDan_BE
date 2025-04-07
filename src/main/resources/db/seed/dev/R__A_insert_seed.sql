INSERT INTO tech_stack (name, image_url)
VALUES ('Java', 'https://devcouse4-team15-bucket.s3.ap-northeast-2.amazonaws.com/techstack/java.svg'),
       ('Kotlin', 'https://devcouse4-team15-bucket.s3.ap-northeast-2.amazonaws.com/techstack/kotlin-1.svg'),
       ('Spring', 'https://devcouse4-team15-bucket.s3.ap-northeast-2.amazonaws.com/techstack/spring-3.svg'),
       ('Spring Boot', 'https://devcouse4-team15-bucket.s3.ap-northeast-2.amazonaws.com/techstack/spring-boot-1.svg'),
       ('JavaScript', 'https://devcouse4-team15-bucket.s3.ap-northeast-2.amazonaws.com/techstack/javascript-2.svg'),
       ('React', 'https://devcouse4-team15-bucket.s3.ap-northeast-2.amazonaws.com/techstack/react-2.svg'),
       ('Typescript', 'https://devcouse4-team15-bucket.s3.ap-northeast-2.amazonaws.com/techstack/typescript.svg'),
       ('Django', 'https://devcouse4-team15-bucket.s3.ap-northeast-2.amazonaws.com/techstack/django.svg'),
       ('MySQL', 'https://devcouse4-team15-bucket.s3.ap-northeast-2.amazonaws.com/techstack/mysql-3.svg'),
       ('MariaDB', 'https://devcouse4-team15-bucket.s3.ap-northeast-2.amazonaws.com/techstack/mariadb.svg'),
       ('PostgreSQL', 'https://devcouse4-team15-bucket.s3.ap-northeast-2.amazonaws.com/techstack/postgresql.svg'),
       ('MongoDB', 'https://devcouse4-team15-bucket.s3.ap-northeast-2.amazonaws.com/techstack/mongodb-icon-1.svg'),
       ('AWS', 'https://devcouse4-team15-bucket.s3.ap-northeast-2.amazonaws.com/techstack/aws-2.svg'),
       ('Node.js', 'https://devcouse4-team15-bucket.s3.ap-northeast-2.amazonaws.com/techstack/nodejs.svg');

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
