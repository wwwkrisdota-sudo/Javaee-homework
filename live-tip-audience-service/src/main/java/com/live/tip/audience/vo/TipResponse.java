package com.live.tip.audience.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TipResponse {

    private Long tipId;
    private String tipNo;
    private Long anchorId;
    private Long audienceId;
    private BigDecimal amount;
    private String traceId;
    private Integer financeSyncStatus;
}
