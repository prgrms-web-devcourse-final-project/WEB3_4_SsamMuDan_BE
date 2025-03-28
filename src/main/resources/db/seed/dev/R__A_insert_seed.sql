INSERT INTO auth_provider (name)
VALUES ('KAKAO'),
       ('NAVER'),
       ('GOOGLE');

INSERT INTO tech_stack (name, image_url)
VALUES ('Java', NULL),
       ('Spring', NULL),
       ('Spring Boot', NULL),
       ('Spring Framework', NULL),
       ('JavaScript', NULL),
       ('React', NULL),
       ('Node.js', NULL);

INSERT INTO development_position (name)
VALUES ('프론트엔드'),
       ('백엔드'),
       ('풀스택');

INSERT INTO education_level (name)
VALUES ('입문'),
       ('초급'),
       ('중급');

INSERT INTO education_category (name)
VALUES ('TechTube'),
       ('TechBook');

INSERT INTO techEducation_type (name)
VALUES ('백엔드'),
       ('프론트엔드'),
       ('웹 개발'),
       ('데이터'),
       ('프로그래밍 언어'),
       ('자료구조/알고리즘'),
       ('개발도구'),
       ('모바일');

INSERT INTO order_category (name)
VALUES ('TechTube'),
       ('TechBook');

INSERT INTO community_category (name)
VALUES ('게시판'),
       ('코드 리뷰');