CREATE TABLE IF NOT EXISTS review
(
    id            BINARY(16)                NOT NULL            COMMENT 'ID',
    zero_drinks   JSON                      NOT NULL            COMMENT '제로음료 종류',
    content       VARCHAR(255)              NOT NULL            COMMENT '판매점 종류',
    user_id       BINARY(16)                NULL                COMMENT '사용자 ID',
    store_id      BINARY(16)                NULL                COMMENT '판매점 ID',
    created_at    DATETIME                  NULL                COMMENT '생성일시',
    updated_at    DATETIME                  NULL                COMMENT '수정일시',
    deleted       BOOLEAN                   NOT NULL DEFAULT 0  COMMENT '삭제여부 0:미삭제, 1:삭제',
    PRIMARY KEY (id),
    INDEX `review_index_user_id` (user_id),
    INDEX `review_index_store_id` (store_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT '리뷰';