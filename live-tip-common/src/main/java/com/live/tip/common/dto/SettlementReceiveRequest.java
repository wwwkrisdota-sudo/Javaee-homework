package com.live.tip.common.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 财务结算接收请求（audience → finance）
 */
@Data
public class SettlementReceiveRequest {

    private Long tipId;
    private String tipNo;
    private Long anchorId;
    private Long audienceId;
    private BigDecimal amount;
    private LocalDateTime tipTime;
}
