package com.live.tip.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("audience_tip_summary")
public class AudienceTipSummary {

    @TableId
    private Long audienceId;
    private BigDecimal totalTipAmount;
    private Integer tipCount;
    private Integer profileLevel;
    private LocalDateTime updateTime;
}
