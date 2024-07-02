CREATE TABLE IF NOT EXISTS store
(
    id            BINARY(16)                NOT NULL            COMMENT 'ID',
    name          VARCHAR(255)              NOT NULL            COMMENT '판매점 이름',
    category      VARCHAR(255)              NOT NULL            COMMENT '판매점 종류',
    address       VARCHAR(255)              NULL                COMMENT '판매점 주소',
    roadAddress   VARCHAR(255)              NULL                COMMENT '도로명 주소',
    mapx          INT                       NOT NULL            COMMENT '주소 - x좌표',
    mapy          INT                       NOT NULL            COMMENT '주소 - y좌표',
    status        BOOLEAN                   NULL                COMMENT '판매 여부 0:미판매, 1:판매',
    images        JSON                      NULL                COMMENT '이미지 목록',
    user_id       BINARY(16)                NULL                COMMENT '사용자 ID',
    created_at    DATETIME                  NULL                COMMENT '생성일시',
    updated_at    DATETIME                  NULL                COMMENT '수정일시',
    deleted       BOOLEAN                   NOT NULL DEFAULT 0  COMMENT '삭제여부 0:미삭제, 1:삭제',
    PRIMARY KEY (id),
    INDEX `store_index_name` (name),
    INDEX `store_index_category` (category),
    INDEX `store_index_address` (address),
    INDEX `store_index_roadAddress` (roadAddress),
    INDEX `store_index_mapx` (mapx),
    INDEX `store_index_mapy` (mapy),
    INDEX `store_index_status` (status),
    INDEX `store_index_user_id` (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT '판매점';
