ALTER TABLE member
    ADD CONSTRAINT unique_email UNIQUE (email);

ALTER TABLE member
    ADD CONSTRAINT unique_phone_number UNIQUE (phone_number);
