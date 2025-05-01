# Date :            2025-04-29
# Author :          강성욱
# Description :     커뮤니티 글 목록 조회 성능 최적화를 위한 Index 처리

# ALTER TABLE community DROP INDEX idx_community_createdAt;
CREATE INDEX idx_community_createdAt ON community (created_at DESC);

# 해당 부분, MySQL 런타임에서는 변경 불가능하여, my.cnf 에 명시 진행
# SET GLOBAL ngram_token_size = 2; // mysql init 시 처리 필요.
# SET GLOBAL ft_min_word_len = 2; // mysql init 시 처리 필요.
# SET GLOBAL innodb_ft_min_token_size = 2; // mysql init 시 처리 필요.
# SET GLOBAL innodb_ft_enable_stopword = 1; // mysql init 시 처리 필요.

# ALTER TABLE community DROP INDEX idx_title_content;
ALTER TABLE community ADD FULLTEXT idx_title_content (title, content) WITH PARSER ngram;
