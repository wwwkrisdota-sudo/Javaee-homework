package com.live.tip.audience.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TopTipperVO {

    private Long audienceId;
    private String audienceName;
    private BigDecimal totalAmount;
    private Integer rank;
}
