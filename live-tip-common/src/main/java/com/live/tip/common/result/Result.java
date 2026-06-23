package com.live.tip.common.result;

import com.live.tip.common.constant.CommonConstants;
import lombok.Data;
import org.slf4j.MDC;

import java.io.Serializable;

/**
 * 统一 API 响应体
 */
@Data
public class Result<T> implements Serializable {

    private int code;
    private String message;
    private T data;
    private String traceId;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMessage());
        result.setData(data);
        result.setTraceId(MDC.get(CommonConstants.MDC_TRACE_ID));
        return result;
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> fail(ResultCode resultCode) {
        Result<T> result = new Result<>();
        result.setCode(resultCode.getCode());
        result.setMessage(resultCode.getMessage());
        result.setTraceId(MDC.get(CommonConstants.MDC_TRACE_ID));
        return result;
    }

    public static <T> Result<T> fail(int code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setTraceId(MDC.get(CommonConstants.MDC_TRACE_ID));
        return result;
    }
}
