package com.live.tip.common.constant;

/**
 * 全局常量
 */
public final class CommonConstants {

    private CommonConstants() {
    }

    /** 链路追踪 Header */
    public static final String TRACE_ID_HEADER = "X-Trace-Id";

    /** MDC 中的 traceId 键 */
    public static final String MDC_TRACE_ID = "traceId";

    /** 服务名称 */
    public static final String SERVICE_GATEWAY = "live-tip-gateway";
    public static final String SERVICE_AUDIENCE = "live-tip-audience-service";
    public static final String SERVICE_FINANCE = "live-tip-finance-service";
    public static final String SERVICE_ANALYTICS = "live-tip-analytics-service";
    public static final String SERVICE_MOCK = "live-tip-mock-service";
}
