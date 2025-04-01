INSERT INTO community (community_category_id, member_id, title, content, view_count, created_at, modified_at)
VALUES
    (1, 1, '1번유저의 게시판 제목', '1번유저의 게시판 글 내용입니다.', 10, NOW(), NOW()),
    (2, 1, '1번유저의 코드리뷰 제목', '1번유저의 코드리뷰 글 내용입니다.', 5, NOW(), NOW()),

    (1, 2, '2번유저의 게시판 제목', '2번유저의 게시판 글 내용입니다.', 12, NOW(), NOW()),
    (2, 2, '2번유저의 코드리뷰 제목', '2번유저의 코드리뷰 글 내용입니다.', 7, NOW(), NOW()),

    (1, 3, '3번유저의 게시판 제목', '3번유저의 게시판 글 내용입니다.', 15, NOW(), NOW()),
    (2, 3, '3번유저의 코드리뷰 제목', '3번유저의 코드리뷰 글 내용입니다.', 8, NOW(), NOW()),

    (1, 4, '4번유저의 게시판 제목', '4번유저의 게시판 글 내용입니다.', 9, DATE_SUB(NOW(), INTERVAL 1 YEAR), DATE_SUB(NOW(), INTERVAL 1 YEAR)),
    (2, 4, '4번유저의 코드리뷰 제목', '4번유저의 코드리뷰 글 내용입니다.', 6, DATE_SUB(NOW(), INTERVAL 1 YEAR), DATE_SUB(NOW(), INTERVAL 1 YEAR)),

    (1, 5, '5번유저의 게시판 제목', '5번유저의 게시판 글 내용입니다.', 20, DATE_SUB(NOW(), INTERVAL 1 YEAR), DATE_SUB(NOW(), INTERVAL 1 YEAR)),
    (2, 5, '5번유저의 코드리뷰 제목', '5번유저의 코드리뷰 글 내용입니다.', 14, DATE_SUB(NOW(), INTERVAL 1 YEAR), DATE_SUB(NOW(), INTERVAL 1 YEAR)),

    (1, 6, '6번유저의 게시판 제목', '6번유저의 게시판 글 내용입니다.', 11, DATE_SUB(NOW(), INTERVAL 1 YEAR), DATE_SUB(NOW(), INTERVAL 1 YEAR)),
    (2, 6, '6번유저의 코드리뷰 제목', '6번유저의 코드리뷰 글 내용입니다.', 9, NOW(), NOW()),

    (1, 7, '7번유저의 게시판 제목', '7번유저의 게시판 글 내용입니다.', 18, NOW(), NOW()),
    (2, 7, '7번유저의 코드리뷰 제목', '7번유저의 코드리뷰 글 내용입니다.', 10, NOW(), NOW()),

    (1, 8, '8번유저의 게시판 제목', '8번유저의 게시판 글 내용입니다.', 13, NOW(), NOW()),
    (2, 8, '8번유저의 코드리뷰 제목', '8번유저의 코드리뷰 글 내용입니다.', 11, NOW(), NOW()),

    (1, 9, '9번유저의 게시판 제목', '9번유저의 게시판 글 내용입니다.', 22, NOW(), NOW()),
    (2, 9, '9번유저의 코드리뷰 제목', '9번유저의 코드리뷰 글 내용입니다.', 16, NOW(), NOW()),

    (1, 10, '10번유저의 게시판 제목', '10번유저의 게시판 글 내용입니다.', 17, NOW(), NOW()),
    (2, 10, '10번유저의 코드리뷰 제목', '10번유저의 코드리뷰 글 내용입니다.', 12, NOW(), NOW());
