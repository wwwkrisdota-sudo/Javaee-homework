package com.live.tip.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("finance_settlement")
public class FinanceSettlement {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long tipId;
    private String tipNo;
    private Long anchorId;
    private Long audienceId;
    private BigDecimal tipAmount;
    private BigDecimal commissionRate;
    private BigDecimal platformAmount;
    private BigDecimal anchorAmount;
    private LocalDateTime tipTime;
    private LocalDateTime settlementTime;
    private LocalDateTime createTime;
}
