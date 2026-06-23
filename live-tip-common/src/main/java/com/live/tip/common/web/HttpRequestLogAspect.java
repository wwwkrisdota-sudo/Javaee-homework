package com.live.tip.common.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.live.tip.common.constant.CommonConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP 请求日志切面：记录请求参数并与 traceId 关联
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class HttpRequestLogAspect {

    private final ObjectMapper objectMapper;

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restControllerPointcut() {
    }

    @Around("restControllerPointcut()")
    public Object logRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String uri = "unknown";
        String method = "unknown";
        String clientIp = "unknown";
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            uri = request.getRequestURI();
            method = request.getMethod();
            clientIp = request.getRemoteAddr();
        }

        String traceId = MDC.get(CommonConstants.MDC_TRACE_ID);
        Map<String, Object> logData = new HashMap<>();
        logData.put("traceId", traceId);
        logData.put("uri", uri);
        logData.put("method", method);
        logData.put("clientIp", clientIp);
        logData.put("handler", joinPoint.getSignature().toShortString());
        logData.put("args", joinPoint.getArgs());

        log.info("HTTP请求开始: {}", objectMapper.writeValueAsString(logData));

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;
            log.info("HTTP请求完成: traceId={}, uri={}, durationMs={}", traceId, uri, duration);
            return result;
        } catch (Throwable ex) {
            long duration = System.currentTimeMillis() - start;
            log.error("HTTP请求异常: traceId={}, uri={}, durationMs={}", traceId, uri, duration, ex);
            throw ex;
        }
    }
}
