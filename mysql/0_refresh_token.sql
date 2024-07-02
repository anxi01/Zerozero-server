CREATE TABLE IF NOT EXISTS refresh_token
(
    id                  BINARY(16)                NOT NULL            COMMENT 'ID',
    refresh_token       VARCHAR(255)              NOT NULL            COMMENT '리프레시 토큰',
    user_id             BINARY(16)                NULL                COMMENT '사용자 ID',
    created_at          DATETIME                  NULL                COMMENT '생성일시',
    updated_at          DATETIME                  NULL                COMMENT '수정일시',
    deleted             BOOLEAN                   NOT NULL DEFAULT 0  COMMENT '삭제여부 0:미삭제, 1:삭제',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT '리프레시 토큰';