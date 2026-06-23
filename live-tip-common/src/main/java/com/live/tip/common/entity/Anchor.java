package com.live.tip.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("anchor")
public class Anchor {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer gender;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
