package com.live.tip.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("tip_hourly_stat")
public class TipHourlyStat {

    @TableId(type = IdType.AUTO)
    private Long id;
    private LocalDateTime statHour;
    private Long anchorId;
    private Integer audienceGender;
    private BigDecimal totalAmount;
    private Integer tipCount;
    private LocalDateTime updateTime;
}
