package com.live.tip.audience.client;

import com.live.tip.audience.client.fallback.AnalyticsClientFallbackFactory;
import com.live.tip.common.constant.CommonConstants;
import com.live.tip.common.dto.AudienceProfileVO;
import com.live.tip.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = CommonConstants.SERVICE_ANALYTICS, path = "/api/analytics",
        fallbackFactory = AnalyticsClientFallbackFactory.class)
public interface AnalyticsClient {

    @GetMapping("/audience/{audienceId}/profile")
    Result<AudienceProfileVO> getProfile(@PathVariable("audienceId") Long audienceId);
}
