-- 최신순 정렬 최적화를 위한 복합 인덱스
CREATE INDEX idx_type_item_created_at
    ON techEducation_review (tech_education_type_id, item_id, created_at DESC);
