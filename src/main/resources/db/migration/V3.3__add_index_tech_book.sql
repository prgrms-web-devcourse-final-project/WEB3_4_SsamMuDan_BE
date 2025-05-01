CREATE INDEX idx_tech_book_created_at USING BTREE ON tech_book (created_at);
CREATE FULLTEXT INDEX idx_tech_book_title_description_introduction ON tech_book (title, description, introduction) WITH PARSER ngram;
CREATE INDEX idx_like_created_at USING BTREE ON like (created_at);