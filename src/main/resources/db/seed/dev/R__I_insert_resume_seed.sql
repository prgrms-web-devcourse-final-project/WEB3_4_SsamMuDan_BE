INSERT INTO resume (id, member_id, email, profile_image, introduction, years, is_open, view_count, created_at,
                    updated_at)
VALUES (1, '1', 'test1@email.com', 'https://loremflickr.com/320/240',
        '저는 개발을 사랑하는 개발자입니다.', 5, 1, 121, '2025-04-03 14:20:10', '2025-04-03 14:20:10'),
       (2, '1', 'test2@email.com', 'https://randomuser.me/api/portraits/men/1.jpg',
        '백엔드 개발자로서 새로운 도전을 즐깁니다.', 0, 1, 95, '2025-04-03 14:21:15', '2025-04-03 14:21:15'),
       (3, '1', 'test3@email.com', 'https://randomuser.me/api/portraits/women/2.jpg',
        '데이터 분석과 AI에 관심이 많습니다.', 10, 1, 110, '2025-04-03 14:22:30', '2025-04-03 14:22:30'),
       (4, '1', 'test4@email.com', 'https://randomuser.me/api/portraits/men/3.jpg',
        '프론트엔드와 UI/UX 디자인을 연구하고 있습니다.', 3, 0, 85, '2025-04-03 14:23:40', '2025-04-03 14:23:40'),
       (5, '1', 'test5@email.com', 'https://randomuser.me/api/portraits/women/4.jpg',
        '최고의 코드를 작성하는 것이 목표입니다.', 10, 1, 230, '2025-04-03 14:24:55', '2025-04-03 14:24:55');

INSERT INTO resume_techStack (id, resume_id, tech_stack_id)
VALUES (1, 1, 1),
       (2, 1, 2),
       (3, 1, 3),
       (4, 2, 4),
       (5, 2, 5),
       (6, 3, 1),
       (7, 3, 2),
       (8, 3, 3),
       (9, 3, 4),
       (10, 4, 1),
       (11, 5, 2);

INSERT INTO career (id, resume_id, position, company, description, is_working, start_date, end_date, created_at,
                    modified_at)
VALUES (1, 1, 벡엔드 서버 개발, 삼성, 삼성증권 모바일 부서 근무, 0,
        2020 - 01 - 01, 2021 - 12 - 31, 2025 - 04 - 02 09:22:13, 2025 - 04 - 02 09:22:13),
       (2, 1, 웹 퍼블리셔, 엘지, 엘지전자 앱 개발, 0,
        2022 - 01 - 01, 2023 - 12 - 31, 2025 - 04 - 02 09:22:13, 2025 - 04 - 02 09:22:13),
       (3, 2, 서버, A, 배달의 민족 백 오피스, 0,
        2020 - 01 - 01, 2021 - 12 - 31, 2025 - 04 - 02 09:23:35, 2025 - 04 - 02 09:23:35),
       (4, 2, 웹 퍼블리셔, 한화, 한화 에너지 그문, 0,
        2022 - 01 - 01, 2023 - 12 - 31, 2025 - 04 - 02 09:23:35, 2025 - 04 - 02 09:23:35),
       (5, 3, 웹 서버 개발, SI, SI 업무 담당, 0,
        2020 - 01 - 01, 2021 - 01 - 01, 2025 - 04 - 03 14:20:10, 2025 - 04 - 03 14:20:10),
       (6, 3, 프론트엔드 개발, 네이버, 네이버 검색 최적화, 0,
        2021 - 02 - 01, 2023 - 01 - 01, 2025 - 04 - 03 14:22:10, 2025 - 04 - 03 14:22:10),
       (7, 4, 데이터 엔지니어, 카카오, 데이터 분석 경험, 0,
        2019 - 06 - 01, 2022 - 06 - 01, 2025 - 04 - 03 14:23:10, 2025 - 04 - 03 14:23:10),
       (8, 4, 백엔드 개발, 라인, 서비스 운영 경험, 0,
        2022 - 07 - 01, 2024 - 03 - 01, 2025 - 04 - 03 14:23:10, 2025 - 04 - 03 14:23:10),
       (9, 5, 게임 개발, 넥슨, 게임 서버 개발 참여, 0,
        2018 - 01 - 01, 2020 - 12 - 31, 2025 - 04 - 03 14:24:10, 2025 - 04 - 03 14:24:10),
       (10, 5, AI 개발, 삼성, 머신러닝 연구, 0,
        2021 - 03 - 01, 2023 - 05 - 01, 2025 - 04 - 03 14:24:10, 2025 - 04 - 03 14:24:10);

INSERT INTO career_techStack (id, career_id, tech_stack_id)
VALUES (1, 1, 3),
       (2, 1, 7),
       (3, 2, 12),
       (4, 2, 9),
       (5, 3, 1),
       (6, 3, 4),
       (7, 4, 10),
       (8, 4, 8),
       (9, 5, 14),
       (10, 5, 6),
       (11, 6, 2),
       (12, 6, 13),
       (13, 7, 5),
       (14, 7, 1),
       (15, 8, 11),
       (16, 8, 3),
       (17, 9, 7),
       (18, 9, 12),
       (19, 10, 9),
       (20, 10, 2);

INSERT INTO portfolio (id, resume_id, name, escription, is_developing, start_date, end_date, created_at, modified_at)
VALUES (1, 1, 개인 블로그, 기술 블로그 개발, 0, 2019 - 06 - 01, 2020 - 05 - 30, 2025 - 04 - 03 15:00:10, 2025 - 04 - 03 15:00:10),
       (2, 1, 사이드 프로젝트, 간단한 채팅 앱, 1, 2023 - 07 - 15, , 2025 - 04 - 03 15:00:10, 2025 - 04 - 03 15:00:10),
       (3, 2, 오픈소스 기여, 깃허브 프로젝트 PR, 0, 2021 - 02 - 01, 2022 - 04 - 10, 2025 - 04 - 03 15:00:10,
        2025 - 04 - 03 15:00:10),
       (4, 2, 프리랜서 개발, 쇼핑몰 웹사이트, 1, 2024 - 01 - 01, , 2025 - 04 - 03 15:00:10, 2025 - 04 - 03 15:00:10),
       (5, 3, 사이드 프로젝트, 간단한 게시판 앱, 0, 2020 - 01 - 01, 2021 - 01 - 01, 2025 - 04 - 03 15:00:10, 2025 - 04 - 03 15:00:10),
       (6, 3, 토이 프로젝트, 할 일 관리 앱, 0, 2022 - 05 - 01, 2023 - 06 - 30, 2025 - 04 - 03 15:00:10, 2025 - 04 - 03 15:00:10),
       (7, 4, 회사 프로젝트, 사내 관리 시스템, 0, 2018 - 03 - 01, 2019 - 12 - 31, 2025 - 04 - 03 15:00:10, 2025 - 04 - 03 15:00:10),
       (8, 5, 프리랜서 작업, 기업 홈페이지 제작, 1, 2023 - 10 - 01, , 2025 - 04 - 03 15:00:10, 2025 - 04 - 03 15:00:10);

INSERT INTO portfolio_techStack (id, portfolio_id, tech_stack_id)
VALUES (1, 1, 4),
       (2, 1, 7),
       (3, 2, 3),
       (4, 2, 1),
       (5, 3, 4),
       (6, 3, 6),
       (7, 4, 7),
       (8, 5, 10),
       (9, 5, 12),
       (10,6,1),
       (11,7,1),
       (12,8,2);
