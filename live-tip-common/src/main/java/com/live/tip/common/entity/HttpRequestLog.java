package com.live.tip.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("http_request_log")
public class HttpRequestLog {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String traceId;
    private String serviceName;
    private String requestUri;
    private String requestMethod;
    private String requestParams;
    private String clientIp;
    private Integer responseCode;
    private Long durationMs;
    private LocalDateTime createTime;
}
