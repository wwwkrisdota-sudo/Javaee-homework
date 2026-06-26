package com.live.tip.audience.service;

import com.live.tip.audience.client.AnalyticsClient;
import com.live.tip.audience.mapper.AudienceMapper;
import com.live.tip.common.dto.AudienceProfileVO;
import com.live.tip.common.entity.Audience;
import com.live.tip.common.exception.BusinessException;
import com.live.tip.common.result.Result;
import com.live.tip.common.result.ResultCode;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final AnalyticsClient analyticsClient;
    private final AudienceMapper audienceMapper;

    @CircuitBreaker(name = "analyticsProfile", fallbackMethod = "getProfileFallback")
    public Result<AudienceProfileVO> getProfile(Long audienceId) {
        Audience audience = audienceMapper.selectById(audienceId);
        if (audience == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "观众不存在");
        }

        Result<AudienceProfileVO> result = analyticsClient.getProfile(audienceId);
        if (result == null) {
            return Result.fail(ResultCode.SERVICE_UNAVAILABLE);
        }
        if (result.getCode() != ResultCode.SUCCESS.getCode()) {
            log.warn("画像查询降级, audienceId={}, code={}, message={}",
                    audienceId, result.getCode(), result.getMessage());
            return result;
        }
        return result;
    }

    @SuppressWarnings("unused")
    private Result<AudienceProfileVO> getProfileFallback(Long audienceId, Throwable throwable) {
        log.warn("画像查询熔断降级, audienceId={}, reason={}", audienceId, throwable.getMessage());
        return Result.fail(ResultCode.SERVICE_UNAVAILABLE);
    }
}
