package com.live.tip.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("anchor_commission_rate")
public class AnchorCommissionRate {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long anchorId;
    private BigDecimal commissionRate;
    private LocalDateTime effectiveTime;
    private LocalDateTime createTime;
}
