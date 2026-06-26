package com.live.tip.audience.client.fallback;

import com.live.tip.audience.client.AnalyticsClient;
import com.live.tip.common.dto.AudienceProfileVO;
import com.live.tip.common.result.Result;
import com.live.tip.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AnalyticsClientFallbackFactory implements FallbackFactory<AnalyticsClient> {

    @Override
    public AnalyticsClient create(Throwable cause) {
        return audienceId -> {
            log.warn("analytics-service 调用失败, audienceId={}, reason={}", audienceId, cause.getMessage());
            return Result.fail(ResultCode.SERVICE_UNAVAILABLE);
        };
    }
}
