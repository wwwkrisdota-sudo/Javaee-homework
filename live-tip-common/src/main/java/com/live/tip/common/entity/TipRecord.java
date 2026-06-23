package com.live.tip.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("tip_record")
public class TipRecord {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String tipNo;
    private Long anchorId;
    private Long audienceId;
    private BigDecimal amount;
    private LocalDateTime tipTime;
    private String traceId;
    private Integer financeSyncStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
