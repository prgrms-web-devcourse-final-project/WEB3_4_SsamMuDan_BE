INSERT INTO comment (parent_id, member_id, community_id, resume_id, content, created_at, modified_at)
VALUES
    -- 일반 댓글 (최상위 댓글)
    (NULL, 1, 1, NULL, '1번 회원의 1번 커뮤니티글 댓글 내용', NOW(), NOW()),
    (NULL, 1, 1, NULL, '1번 회원의 1번 커뮤니티글 댓글 내용', NOW(), NOW()),
    (NULL, 1, 1, NULL, '1번 회원의 1번 커뮤니티글 댓글 내용', NOW(), NOW()),
    (NULL, 2, 1, NULL, '2번 회원의 1번 커뮤니티글 댓글 내용', NOW(), NOW()),

    (NULL, 1, 2, NULL, '1번 회원의 2번 커뮤니티글 댓글 내용', NOW(), NOW()),
    (NULL, 2, 2, NULL, '2번 회원의 2번 커뮤니티글 댓글 내용', NOW(), NOW()),

    (NULL, 1, 3, NULL, '1번 회원의 3번 커뮤니티글 댓글 내용', NOW(), NOW()),
    (NULL, 2, 3, NULL, '2번 회원의 3번 커뮤니티글 댓글 내용', NOW(), NOW()),

    (NULL, 1, 4, NULL, '1번 회원의 4번 커뮤니티글 댓글 내용', NOW(), NOW()),
    (NULL, 2, 4, NULL, '2번 회원의 4번 커뮤니티글 댓글 내용', NOW(), NOW()),

    (NULL, 1, 5, NULL, '1번 회원의 5번 커뮤니티글 댓글 내용', NOW(), NOW()),
    (NULL, 2, 5, NULL, '2번 회원의 5번 커뮤니티글 댓글 내용', NOW(), NOW()),
    (NULL, 2, 5, NULL, '2번 회원의 5번 커뮤니티글 댓글 내용', NOW(), NOW()),
    (NULL, 2, 5, NULL, '2번 회원의 5번 커뮤니티글 댓글 내용', NOW(), NOW()),
    (NULL, 2, 5, NULL, '2번 회원의 5번 커뮤니티글 댓글 내용', NOW(), NOW()),
    (NULL, 2, 5, NULL, '2번 회원의 5번 커뮤니티글 댓글 내용', NOW(), NOW()),
    (NULL, 2, 5, NULL, '2번 회원의 5번 커뮤니티글 댓글 내용', NOW(), NOW());