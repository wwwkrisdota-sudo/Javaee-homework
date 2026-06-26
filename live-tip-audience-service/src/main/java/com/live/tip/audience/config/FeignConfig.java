package com.live.tip.audience.config;

import com.live.tip.common.constant.CommonConstants;
import feign.RequestInterceptor;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor traceIdRequestInterceptor() {
        return template -> {
            String traceId = MDC.get(CommonConstants.MDC_TRACE_ID);
            if (traceId != null) {
                template.header(CommonConstants.TRACE_ID_HEADER, traceId);
            }
        };
    }
}
