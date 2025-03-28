# Date :            2025-03-28
# Author :          Baekgwa
# Description :     커뮤니티 Table `view_count` Column int->bigint 로 변환. 사유 : viewcount 는 많이 증가할 수 있어, 오버플로우 발생할 수 있을 것 같음.

ALTER TABLE community
    MODIFY COLUMN view_count bigint DEFAULT 0 NOT NULL;
