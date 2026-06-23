-- ============================================================
-- 直播打赏微服务系统 - 数据库表结构
-- 数据库: live_tip
-- 字符集: utf8mb4
-- ============================================================

CREATE DATABASE IF NOT EXISTS live_tip
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE live_tip;

-- ------------------------------------------------------------
-- 主播表
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS anchor (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主播ID',
    name            VARCHAR(64)     NOT NULL COMMENT '主播姓名',
    gender          TINYINT         NOT NULL DEFAULT 0 COMMENT '性别: 0-未知 1-男 2-女',
    status          TINYINT         NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用 1-正常',
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_anchor_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='主播表';

-- ------------------------------------------------------------
-- 主播分成比例历史（变化前按旧比例，变化后按新比例）
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS anchor_commission_rate (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    anchor_id       BIGINT          NOT NULL COMMENT '主播ID',
    commission_rate DECIMAL(5, 4)   NOT NULL COMMENT '平台抽成比例(0~1)，主播得到 tip*(1-rate)',
    effective_time  DATETIME        NOT NULL COMMENT '生效时间（含）',
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_anchor_effective (anchor_id, effective_time DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='主播分成比例历史';

-- ------------------------------------------------------------
-- 观众表
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS audience (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '观众ID',
    name            VARCHAR(64)     NOT NULL COMMENT '观众昵称',
    gender          TINYINT         NOT NULL DEFAULT 0 COMMENT '性别: 0-未知 1-男 2-女',
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='观众表';

-- ------------------------------------------------------------
-- 打赏记录（核心表，tip_no 幂等防重复）
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS tip_record (
    id                  BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    tip_no              VARCHAR(64)     NOT NULL COMMENT '打赏业务单号/幂等键',
    anchor_id           BIGINT          NOT NULL COMMENT '主播ID',
    audience_id         BIGINT          NOT NULL COMMENT '观众ID',
    amount              DECIMAL(12, 2)  NOT NULL COMMENT '打赏金额',
    tip_time            DATETIME        NOT NULL COMMENT '打赏时间',
    trace_id            VARCHAR(64)     DEFAULT NULL COMMENT '链路追踪ID',
    finance_sync_status TINYINT         NOT NULL DEFAULT 0 COMMENT '财务同步: 0-待同步 1-已同步 2-失败',
    create_time         DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time         DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_tip_no (tip_no),
    KEY idx_anchor_time (anchor_id, tip_time),
    KEY idx_audience (audience_id),
    KEY idx_tip_time (tip_time),
    KEY idx_finance_sync (finance_sync_status, id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='打赏记录表';

-- ------------------------------------------------------------
-- 财务结算明细（每笔打赏一条，tip_id 唯一防重复结算）
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS finance_settlement (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    tip_id          BIGINT          NOT NULL COMMENT '关联 tip_record.id',
    tip_no          VARCHAR(64)     NOT NULL COMMENT '打赏单号',
    anchor_id       BIGINT          NOT NULL COMMENT '主播ID',
    audience_id     BIGINT          NOT NULL COMMENT '观众ID',
    tip_amount      DECIMAL(12, 2)  NOT NULL COMMENT '打赏金额',
    commission_rate DECIMAL(5, 4)   NOT NULL COMMENT '结算时平台抽成比例',
    platform_amount DECIMAL(12, 2)  NOT NULL COMMENT '平台分成金额',
    anchor_amount   DECIMAL(12, 2)  NOT NULL COMMENT '主播分成金额',
    tip_time        DATETIME        NOT NULL COMMENT '打赏时间',
    settlement_time DATETIME        NOT NULL COMMENT '结算时间',
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_tip_id (tip_id),
    KEY idx_anchor_settlement (anchor_id, settlement_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='财务结算明细';

-- ------------------------------------------------------------
-- 主播余额汇总
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS anchor_balance (
    anchor_id           BIGINT          NOT NULL COMMENT '主播ID',
    total_tip_amount    DECIMAL(14, 2)  NOT NULL DEFAULT 0.00 COMMENT '累计收到打赏总额',
    total_anchor_amount DECIMAL(14, 2)  NOT NULL DEFAULT 0.00 COMMENT '累计主播分成总额',
    withdrawn_amount    DECIMAL(14, 2)  NOT NULL DEFAULT 0.00 COMMENT '已提取金额',
    available_amount    DECIMAL(14, 2)  NOT NULL DEFAULT 0.00 COMMENT '可提取余额',
    update_time         DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (anchor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='主播余额汇总';

-- ------------------------------------------------------------
-- 观众打赏汇总（用于画像计算）
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS audience_tip_summary (
    audience_id     BIGINT          NOT NULL COMMENT '观众ID',
    total_tip_amount DECIMAL(14, 2) NOT NULL DEFAULT 0.00 COMMENT '累计打赏金额',
    tip_count       INT             NOT NULL DEFAULT 0 COMMENT '打赏次数',
    profile_level   TINYINT         DEFAULT NULL COMMENT '画像: 1-高消费 2-中消费 3-低消费',
    update_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (audience_id),
    KEY idx_profile_level (profile_level),
    KEY idx_total_amount (total_tip_amount DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='观众打赏汇总';

-- ------------------------------------------------------------
-- 小时维度打赏统计（经营分析）
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS tip_hourly_stat (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    stat_hour       DATETIME        NOT NULL COMMENT '统计小时起始时间(整点)',
    anchor_id       BIGINT          NOT NULL COMMENT '主播ID',
    audience_gender TINYINT         NOT NULL COMMENT '观众性别: 1-男 2-女 0-未知',
    total_amount    DECIMAL(14, 2)  NOT NULL DEFAULT 0.00 COMMENT '该小时打赏总额',
    tip_count       INT             NOT NULL DEFAULT 0 COMMENT '打赏笔数',
    update_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_stat_dimension (stat_hour, anchor_id, audience_gender),
    KEY idx_anchor_hour (anchor_id, stat_hour)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='小时维度打赏统计';

-- ------------------------------------------------------------
-- HTTP 请求日志（traceId 关联排查）
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS http_request_log (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    trace_id        VARCHAR(64)     NOT NULL COMMENT '链路追踪ID',
    service_name    VARCHAR(64)     NOT NULL COMMENT '服务名',
    request_uri     VARCHAR(512)    DEFAULT NULL COMMENT '请求URI',
    request_method  VARCHAR(16)     DEFAULT NULL COMMENT 'HTTP方法',
    request_params  TEXT            COMMENT '请求参数(JSON)',
    client_ip       VARCHAR(64)     DEFAULT NULL COMMENT '客户端IP',
    response_code   INT             DEFAULT NULL COMMENT 'HTTP响应码',
    duration_ms     BIGINT          DEFAULT NULL COMMENT '耗时毫秒',
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_trace_id (trace_id),
    KEY idx_service_time (service_name, create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='HTTP请求日志';
