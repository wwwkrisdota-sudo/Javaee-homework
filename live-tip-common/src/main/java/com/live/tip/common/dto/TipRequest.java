package com.live.tip.common.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 打赏请求 DTO（供各服务复用）
 */
@Data
public class TipRequest {

    @NotBlank(message = "打赏单号不能为空")
    private String tipNo;

    @NotNull(message = "主播ID不能为空")
    private Long anchorId;

    @NotNull(message = "观众ID不能为空")
    private Long audienceId;

    @NotNull(message = "打赏金额不能为空")
    @DecimalMin(value = "0.01", message = "打赏金额必须大于0")
    private BigDecimal amount;
}
