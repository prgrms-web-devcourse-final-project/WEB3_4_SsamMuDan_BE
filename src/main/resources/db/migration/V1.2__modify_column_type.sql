# Date :            2025-03-30
# Author :          loadingKKamo21
# Description :     컬럼 타입 수정 및 기본값 설정

-- Modify tech_tube duration column time to bigint
ALTER TABLE tech_tube MODIFY COLUMN tech_tube_duration BIGINT UNSIGNED;

-- Set default value to tech_book/tube total_rating, total_review_count column to 0
ALTER TABLE tech_book MODIFY COLUMN total_rating INTEGER UNSIGNED NOT NULL DEFAULT 0;
ALTER TABLE tech_book MODIFY COLUMN total_review_count INTEGER UNSIGNED NOT NULL DEFAULT 0;
ALTER TABLE tech_tube MODIFY COLUMN total_rating INTEGER UNSIGNED NOT NULL DEFAULT 0;
ALTER TABLE tech_tube MODIFY COLUMN total_review_count INTEGER UNSIGNED NOT NULL DEFAULT 0;

-- Fix order_history product_name column
ALTER TABLE order_history MODIFY COLUMN product_name VARCHAR(255) NOT NULL;