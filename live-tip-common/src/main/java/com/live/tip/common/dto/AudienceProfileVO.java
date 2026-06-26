package com.live.tip.common.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 观众消费画像（analytics 返回，audience 透传）
 */
@Data
public class AudienceProfileVO {

    private Long audienceId;
    private String audienceName;
    private Integer profileLevel;
    private String profileLevelLabel;
    private BigDecimal totalTipAmount;
    private Integer tipCount;
}
