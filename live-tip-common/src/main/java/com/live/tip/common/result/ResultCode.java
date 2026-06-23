package com.live.tip.common.result;

import lombok.Getter;

/**
 * 响应状态码
 */
@Getter
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_ERROR(500, "系统内部错误"),
    SERVICE_UNAVAILABLE(503, "服务暂时不可用，请稍后重试"),
    DUPLICATE_TIP(1001, "重复打赏，请勿重复提交"),
    SETTLEMENT_FAILED(1002, "财务结算失败");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
