package com.live.tip.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("anchor_balance")
public class AnchorBalance {

    @TableId
    private Long anchorId;
    private BigDecimal totalTipAmount;
    private BigDecimal totalAnchorAmount;
    private BigDecimal withdrawnAmount;
    private BigDecimal availableAmount;
    private LocalDateTime updateTime;
}
