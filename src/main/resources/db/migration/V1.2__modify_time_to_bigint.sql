# Date :            2025-03-30
# Author :          loadingKKamo21
# Description :     영상 길이 컬럼 타입 TIME에서 BIGINT로 변경

-- Modify tech_tube duration column time to bigint
ALTER TABLE tech_tube MODIFY COLUMN tech_tube_duration BIGINT UNSIGNED;