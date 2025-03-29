# Date :            2025-03-27
# Author :          강성욱
# Description :     초기 설정

CREATE TABLE `member`
(
    `id`                VARCHAR(255) NOT NULL,
    `email`             VARCHAR(255) NOT NULL,
    `name`              VARCHAR(255) NOT NULL,
    `nickname`          VARCHAR(255) NOT NULL,
    `password`          VARCHAR(255) NOT NULL,
    `phone_number`      VARCHAR(255) NOT NULL,
    `profile_image_url` VARCHAR(255) DEFAULT NULL,
    `role`              ENUM('USER', 'HUNTER', 'ADMIN')       NOT NULL DEFAULT 'USER',
    `member_status`     ENUM('ACTIVE', 'INACTIVE', 'PENDING') NOT NULL DEFAULT 'ACTIVE',
    `created_at`        DATETIME     NOT NULL,
    `modified_at`       DATETIME     NOT NULL,
    PRIMARY KEY `pk_member_id` (`id`),
    UNIQUE KEY `uq_member_nickname` (`nickname`)
);

CREATE TABLE `auth_provider`
(
    `id`   BIGINT UNSIGNED       NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    PRIMARY KEY `pk_auth_provider_id` (`id`)
);

CREATE TABLE `auth`
(
    `id`               BIGINT UNSIGNED       NOT NULL AUTO_INCREMENT,
    `member_id`        VARCHAR(255) NOT NULL,
    `auth_provider_id` BIGINT UNSIGNED       NOT NULL,
    `provider_id`      VARCHAR(255) NOT NULL,
    `created_at`       DATETIME     NOT NULL,
    PRIMARY KEY `pk_auth_id` (`id`),
    FOREIGN KEY `fk_auth_member_id` (`member_id`) REFERENCES `member` (`id`),
    FOREIGN KEY `fk_auth_auth_provider_id` (`auth_provider_id`) REFERENCES `auth_provider` (`id`)
);

CREATE TABLE `resume`
(
    `id`            BIGINT UNSIGNED       NOT NULL AUTO_INCREMENT,
    `member_id`     VARCHAR(255) NOT NULL,
    `email`         VARCHAR(255) NOT NULL,
    `profile_image` VARCHAR(255) NOT NULL,
    `introduction`  TEXT         NOT NULL,
    `years`         INTEGER UNSIGNED      NOT NULL,
    `is_open`       BOOLEAN      NOT NULL DEFAULT TRUE,
    `view_count`    INTEGER UNSIGNED      NOT NULL DEFAULT 0,
    `created_at`    DATETIME     NOT NULL,
    `modified_at`   DATETIME     NOT NULL,
    PRIMARY KEY `pk_resume_id` (`id`),
    FOREIGN KEY `fk_resume_member_id` (`member_id`) REFERENCES `member` (`id`)
);

CREATE TABLE `offer`
(
    `id`          BIGINT UNSIGNED       NOT NULL AUTO_INCREMENT,
    `normal_id`   VARCHAR(255) NOT NULL,
    `hunter_id`   VARCHAR(255) NOT NULL,
    `content`     TEXT         NOT NULL,
    `created_at`  DATETIME     NOT NULL,
    `modified_at` DATETIME     NOT NULL,
    PRIMARY KEY `pk_offer_id` (`id`),
    FOREIGN KEY `fk_offer_nomarl_id` (`normal_id`) REFERENCES `member` (`id`),
    FOREIGN KEY `fk_offer_hunter_id` (`hunter_id`) REFERENCES `member` (`id`)
);

CREATE TABLE `tech_stack`
(
    `id`        BIGINT UNSIGNED       NOT NULL AUTO_INCREMENT,
    `name`      VARCHAR(255) NOT NULL,
    `image_url` VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY `pk_tech_stack_id` (`id`),
    UNIQUE KEY `uq_tech_stack_name` (`name`)
);

CREATE TABLE `resume_techStack`
(
    `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `resume_id`     BIGINT UNSIGNED NOT NULL,
    `tech_stack_id` BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY `pk_resume_tech_stack_id` (`id`),
    FOREIGN KEY `fk_resume_tech_stack_resume_id` (`resume_id`) REFERENCES `resume` (`id`),
    FOREIGN KEY `fk_resume_tech_stack_tech_stack_id` (`tech_stack_id`) REFERENCES `tech_stack` (`id`)
);

CREATE TABLE `development_position`
(
    `id`   BIGINT UNSIGNED       NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    PRIMARY KEY `pk_development_position_id` (`id`),
    UNIQUE KEY `uq_development_position_name` (`name`)
);

CREATE TABLE `resume_developmentPosition`
(
    `id`                      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `resume_id`               BIGINT UNSIGNED NOT NULL,
    `development_position_id` BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY `pk_resume_development_position_id` (`id`),
    FOREIGN KEY `fk_resume_development_resume_id` (`resume_id`) REFERENCES `resume` (`id`),
    FOREIGN KEY `fk_resume_development_development_position_id` (`development_position_id`) REFERENCES `development_position` (`id`)
);

CREATE TABLE `career`
(
    `id`          BIGINT UNSIGNED       NOT NULL AUTO_INCREMENT,
    `resume_id`   BIGINT UNSIGNED       NOT NULL,
    `position`    VARCHAR(255) NOT NULL,
    `company`     VARCHAR(255) NOT NULL,
    `description` TEXT         NOT NULL,
    `is_working`  BOOLEAN      NOT NULL DEFAULT FALSE,
    `start_date`  DATE         NOT NULL,
    `end_date`    DATE                  DEFAULT NULL,
    `created_at`  DATETIME     NOT NULL,
    `modified_at` DATETIME     NOT NULL,
    PRIMARY KEY `pk_career_id` (`id`),
    FOREIGN KEY `fk_career_resume_id` (`resume_id`) REFERENCES `resume` (`id`)
);

CREATE TABLE `career_techStack`
(
    `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `career_id`     BIGINT UNSIGNED NOT NULL,
    `tech_stack_id` BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY `pk_career_tech_stack_id` (`id`),
    FOREIGN KEY `fk_career_tech_stack_career_id` (`career_id`) REFERENCES `career` (`id`),
    FOREIGN KEY `fk_career_tech_stack_tech_stack_id` (`tech_stack_id`) REFERENCES `tech_stack` (`id`)
);

CREATE TABLE `portfolio`
(
    `id`            BIGINT UNSIGNED       NOT NULL AUTO_INCREMENT,
    `resume_id`     BIGINT UNSIGNED       NOT NULL,
    `name`          VARCHAR(255) NOT NULL,
    `description`   TEXT         NOT NULL,
    `is_developing` BOOLEAN      NOT NULL DEFAULT TRUE,
    `start_date`    DATE         NOT NULL,
    `end_date`      DATE                  DEFAULT NULL,
    `created_at`    DATETIME     NOT NULL,
    `modified_at`   DATETIME     NOT NULL,
    PRIMARY KEY `pk_portfolio_id` (`id`),
    FOREIGN KEY `fk_portfolio_resume_id` (`resume_id`) REFERENCES `resume` (`id`)
);

CREATE TABLE `portfolio_techStack`
(
    `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `portfolio_id`  BIGINT UNSIGNED NOT NULL,
    `tech_stack_id` BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY `pk_portfolio_tech_stack_id` (`id`),
    FOREIGN KEY `fk_portfolio_tech_stack_portfolio_id` (`portfolio_id`) REFERENCES `portfolio` (`id`),
    FOREIGN KEY `fk_portfolio_tech_stack_tech_stack_id` (`tech_stack_id`) REFERENCES `tech_stack` (`id`)
);

CREATE TABLE `education_level`
(
    `id`   BIGINT UNSIGNED       NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    PRIMARY KEY `pk_education_level_id` (`id`),
    UNIQUE KEY `uq_education_level_name` (`name`)
);

CREATE TABLE `tech_tube`
(
    `id`                      BIGINT UNSIGNED       NOT NULL AUTO_INCREMENT,
    `member_id`               VARCHAR(255) NOT NULL,
    `education_level_id`      BIGINT UNSIGNED       NOT NULL,
    `title`                   VARCHAR(255) NOT NULL,
    `description`             VARCHAR(255) NOT NULL,
    `introduction`            TEXT         NOT NULL,
    `total_rating`            INTEGER UNSIGNED      NOT NULL,
    `total_review_count`      INTEGER UNSIGNED      NOT NULL,
    `tech_tube_url`           VARCHAR(255) NOT NULL,
    `tech_tube_duration`      TIME         NOT NULL,
    `tech_tube_thumbnail_url` VARCHAR(255) NOT NULL,
    `price`                   INTEGER UNSIGNED      NOT NULL,
    `view_count`              INTEGER UNSIGNED      NOT NULL DEFAULT 0,
    `created_at`              DATETIME     NOT NULL,
    `modified_at`             DATETIME     NOT NULL,
    PRIMARY KEY `pk_tech_tube_id` (`id`),
    FOREIGN KEY `fk_tech_tube_member_id` (`member_id`) REFERENCES `member` (`id`),
    FOREIGN KEY `fk_tech_tube_education_level_id` (`education_level_id`) REFERENCES `education_level` (`id`)
);

CREATE TABLE `education_category`
(
    `id`   BIGINT UNSIGNED       NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    PRIMARY KEY `pk_education_category_id` (`id`),
    UNIQUE KEY `uq_education_category_name` (`name`)
);

CREATE TABLE `techTube_educationCategory`
(
    `id`                    BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `tech_tube_id`          BIGINT UNSIGNED NOT NULL,
    `education_category_id` BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY `pk_tech_tube_education_category_id` (`id`),
    FOREIGN KEY `fk_tech_tube_education_category_tech_tube_id` (`tech_tube_id`) REFERENCES `tech_tube` (`id`),
    FOREIGN KEY `fk_tech_tube_education_category_education_category_id` (`education_category_id`) REFERENCES `education_category` (`id`)
);

CREATE TABLE `techEducation_type`
(
    `id`   BIGINT UNSIGNED       NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    PRIMARY KEY `pk_tech_education_type_id` (`id`),
    UNIQUE KEY `uq_tech_education_type_name` (`name`)
);

CREATE TABLE `techEducation_review`
(
    `id`                     BIGINT UNSIGNED       NOT NULL AUTO_INCREMENT,
    `member_id`              VARCHAR(255) NOT NULL,
    `tech_education_type_id` BIGINT UNSIGNED       NOT NULL,
    `rating`                 INTEGER UNSIGNED      NOT NULL,
    `content`                VARCHAR(255) NOT NULL,
    `item_id`                BIGINT UNSIGNED       NOT NULL,
    `created_at`             DATETIME     NOT NULL,
    `modified_at`            DATETIME     NOT NULL,
    PRIMARY KEY `pk_tech_education_review_id` (`id`),
    FOREIGN KEY `fk_tech_education_review_member_id` (`member_id`) REFERENCES `member` (`id`),
    FOREIGN KEY `fk_tech_education_review_tech_education_type_id` (`tech_education_type_id`) REFERENCES `techEducation_type` (`id`)
);

CREATE TABLE `tech_book`
(
    `id`                      BIGINT UNSIGNED       NOT NULL AUTO_INCREMENT,
    `member_id`               VARCHAR(255) NOT NULL,
    `education_level_id`      BIGINT UNSIGNED       NOT NULL,
    `title`                   VARCHAR(255) NOT NULL,
    `description`             VARCHAR(255) NOT NULL,
    `introduction`            TEXT         NOT NULL,
    `total_rating`            INTEGER UNSIGNED      NOT NULL,
    `total_review_count`      INTEGER UNSIGNED      NOT NULL,
    `tech_book_url`           VARCHAR(255) NOT NULL,
    `tech_book_preview_url`   VARCHAR(255) NOT NULL,
    `tech_book_thumbnail_url` VARCHAR(255) NOT NULL,
    `tech_book_page`          INTEGER UNSIGNED      NOT NULL,
    `price`                   INTEGER UNSIGNED      NOT NULL,
    `view_count`              INTEGER UNSIGNED      NOT NULL DEFAULT 0,
    `created_at`              DATETIME     NOT NULL,
    `modified_at`             DATETIME     NOT NULL,
    PRIMARY KEY `pk_tech_book_id` (`id`),
    FOREIGN KEY `fk_tech_book_member_id` (`member_id`) REFERENCES `member` (`id`),
    FOREIGN KEY `fk_tech_book_edu_level_id` (`education_level_id`) REFERENCES `education_level` (`id`)
);

CREATE TABLE `techBook_educationCategory`
(
    `id`                    BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `tech_book_id`          BIGINT UNSIGNED NOT NULL,
    `education_category_id` BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY `pk_tech_book_education_category_id` (`id`),
    FOREIGN KEY `fk_tech_book_education_category_tech_book_id` (`tech_book_id`) REFERENCES `tech_book` (`id`),
    FOREIGN KEY `fk_tech_book_education_category_education_category_id` (`education_category_id`) REFERENCES `education_category` (`id`)
);

CREATE TABLE `order_category`
(
    `id`   BIGINT UNSIGNED       NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    PRIMARY KEY `pk_product_type_id` (`id`),
    UNIQUE KEY `uq_product_type_name` (`name`)
);

CREATE TABLE `order_history`
(
    `id`                BIGINT UNSIGNED       NOT NULL AUTO_INCREMENT,
    `order_category_id` BIGINT UNSIGNED       NOT NULL,
    `member_id`         VARCHAR(255) NOT NULL,
    `order_id`          VARCHAR(255) NOT NULL,
    `product_id`        BIGINT UNSIGNED       NOT NULL,
    `product_name`      INTEGER UNSIGNED      NOT NULL,
    `price`             INTEGER UNSIGNED      NOT NULL,
    `created_at`        DATETIME     NOT NULL,
    `modified_at`       DATETIME     NOT NULL,
    PRIMARY KEY `pk_order_item_id` (`id`),
    FOREIGN KEY `fk_order_history_member_id` (`member_id`) REFERENCES `member` (`id`),
    FOREIGN KEY `fk_order_history_order_category_id` (`order_category_id`) REFERENCES `order_category` (`id`),
    UNIQUE KEY `uq_order_history_order_id` (`order_id`)
);

CREATE TABLE `project`
(
    `id`                BIGINT UNSIGNED       NOT NULL AUTO_INCREMENT,
    `member_id`         VARCHAR(255) NOT NULL,
    `title`             VARCHAR(255) NOT NULL,
    `description`       TEXT         NOT NULL,
    `contact`           VARCHAR(255) NOT NULL,
    `project_image_url` VARCHAR(255)          DEFAULT NULL,
    `partner_type`      TEXT         NOT NULL,
    `is_open`           BOOLEAN      NOT NULL DEFAULT TRUE,
    `start_date`        DATE         NOT NULL,
    `end_date`          DATE                  DEFAULT NULL,
    `view_count`        INTEGER UNSIGNED      NOT NULL DEFAULT 0,
    `created_at`        DATETIME     NOT NULL,
    `modified_at`       DATETIME     NOT NULL,
    PRIMARY KEY `pk_project_id` (`id`),
    FOREIGN KEY `fk_project_member_id` (`member_id`) REFERENCES `member` (`id`)
);

CREATE TABLE `project_techStack`
(
    `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `project_id`    BIGINT UNSIGNED NOT NULL,
    `tech_stack_id` BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY `pk_project_tech_stack_id` (`id`),
    FOREIGN KEY `fk_project_tech_stack_project_id` (`project_id`) REFERENCES `project` (`id`),
    FOREIGN KEY `fk_project_tech_stack_tech_stack_id` (`tech_stack_id`) REFERENCES `tech_stack` (`id`)
);

CREATE TABLE `project_devPosition`
(
    `id`                      BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT,
    `project_id`              BIGINT UNSIGNED  NOT NULL,
    `development_position_id` BIGINT UNSIGNED  NOT NULL,
    `amount`                  INTEGER UNSIGNED NOT NULL DEFAULT 0,
    PRIMARY KEY `pk_project_development_position_id` (`id`),
    FOREIGN KEY `fk_project_development_position_project_id` (`project_id`) REFERENCES `project` (`id`),
    FOREIGN KEY `fk_project_development_position_development_position_id` (`development_position_id`) REFERENCES `development_position` (`id`)
);

CREATE TABLE `project_membership`
(
    `id`                        BIGINT UNSIGNED       NOT NULL AUTO_INCREMENT,
    `project_id`                BIGINT UNSIGNED       NOT NULL,
    `member_id`                 VARCHAR(255) NOT NULL,
    `project_membership_status` ENUM('PENDING', 'JOINED', 'LEAVED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    `created_at`                DATETIME     NOT NULL,
    `modified_at`               DATETIME     NOT NULL,
    PRIMARY KEY `pk_project_membership_id` (`id`),
    FOREIGN KEY `fk_project_membership_project_id` (`project_id`) REFERENCES `project` (`id`),
    FOREIGN KEY `fk_project_membership_member_id` (`member_id`) REFERENCES `member` (`id`)
);

CREATE TABLE `community_category`
(
    `id`   BIGINT UNSIGNED       NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    PRIMARY KEY `pk_community_category_id` (`id`),
    UNIQUE KEY `uq_community_category_name` (`name`)
);

CREATE TABLE `community`
(
    `id`                    BIGINT UNSIGNED       NOT NULL AUTO_INCREMENT,
    `community_category_id` BIGINT UNSIGNED       NOT NULL,
    `member_id`             VARCHAR(255) NOT NULL,
    `title`                 VARCHAR(255) NOT NULL,
    `content`               TEXT         NOT NULL,
    `view_count`            INTEGER UNSIGNED      NOT NULL DEFAULT 0,
    `created_at`            DATETIME     NOT NULL,
    `modified_at`           DATETIME     NOT NULL,
    PRIMARY KEY `pk_community_id` (`id`),
    FOREIGN KEY `fk_community_member_id` (`member_id`) REFERENCES `member` (`id`),
    FOREIGN KEY `fk_community_community_category_id` (`community_category_id`) REFERENCES `community_category` (`id`)
);

CREATE TABLE `comment`
(
    `id`           BIGINT UNSIGNED       NOT NULL AUTO_INCREMENT,
    `parent_id`    BIGINT UNSIGNED DEFAULT NULL,
    `member_id`    VARCHAR(255) NOT NULL,
    `community_id` BIGINT UNSIGNED DEFAULT NULL,
    `resume_id`    BIGINT UNSIGNED DEFAULT NULL,
    `content`      TEXT         NOT NULL,
    `created_at`   DATETIME     NOT NULL,
    `modified_at`  DATETIME     NOT NULL,
    PRIMARY KEY `pk_comment_id` (`id`),
    FOREIGN KEY `fk_comment_parent_id` (`parent_id`) REFERENCES `comment` (`id`),
    FOREIGN KEY `fk_comment_member_id` (`member_id`) REFERENCES `member` (`id`),
    FOREIGN KEY `fk_comment_community_id` (`community_id`) REFERENCES `community` (`id`),
    FOREIGN KEY `fk_comment_resume_id` (`resume_id`) REFERENCES `resume` (`id`)
);

CREATE TABLE `likes`
(
    `id`           BIGINT UNSIGNED       NOT NULL AUTO_INCREMENT,
    `member_id`    VARCHAR(255) NOT NULL,
    `tech_tube_id` BIGINT UNSIGNED DEFAULT NULL,
    `tech_book_id` BIGINT UNSIGNED DEFAULT NULL,
    `project_id`   BIGINT UNSIGNED DEFAULT NULL,
    `community_id` BIGINT UNSIGNED DEFAULT NULL,
    `created_at`   DATETIME     NOT NULL,
    `modified_at`  DATETIME     NOT NULL,
    PRIMARY KEY `pk_likes_id` (`id`),
    FOREIGN KEY `fk_likes_member_id` (`member_id`) REFERENCES `member` (`id`),
    FOREIGN KEY `fk_likes_tech_book_id` (`tech_book_id`) REFERENCES `tech_book` (`id`),
    FOREIGN KEY `fk_likes_tech_tube_id` (`tech_tube_id`) REFERENCES `tech_tube` (`id`),
    FOREIGN KEY `fk_likes_project_id` (`project_id`) REFERENCES `project` (`id`),
    FOREIGN KEY `fk_likes_community_id` (`community_id`) REFERENCES `community` (`id`)
);