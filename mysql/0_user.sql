CREATE TABLE IF NOT EXISTS user
(
    id            BINARY(16)                NOT NULL            COMMENT 'ID',
    nickname      VARCHAR(255)              NOT NULL            COMMENT '닉네임',
    email         VARCHAR(255)              NOT NULL            COMMENT '이메일',
    password      VARCHAR(255)              NOT NULL            COMMENT '비밀번호',
    profile_image VARCHAR(255)              NULL                COMMENT '프로필 사진',
    role          ENUM ('USER', 'ADMIN')    NOT NULL            COMMENT '권한',
    created_at    DATETIME                  NULL                COMMENT '생성일시',
    updated_at    DATETIME                  NULL                COMMENT '수정일시',
    deleted       BOOLEAN                   NOT NULL DEFAULT 0  COMMENT '삭제여부 0:미삭제, 1:삭제',
    PRIMARY KEY (id),
    INDEX `user_index_nickname` (nickname)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT '사용자';