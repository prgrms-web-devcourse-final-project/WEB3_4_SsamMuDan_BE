ALTER TABLE order_history ADD COLUMN payment_key VARCHAR(255) NOT NULL AFTER member_id;
ALTER TABLE order_history ADD COLUMN payment_status ENUM('PENDING', 'SUCCESS', 'FAILED', 'CANCELED') NOT NULL AFTER payment_key;
ALTER TABLE order_history ADD CONSTRAINT uq_order_history_payment_key UNIQUE (payment_key);