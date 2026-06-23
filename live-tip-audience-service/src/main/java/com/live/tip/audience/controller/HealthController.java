package com.live.tip.audience.controller;

import com.live.tip.common.constant.CommonConstants;
import com.live.tip.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 健康检查（成员二在此基础上实现业务接口）
 */
@RestController
@RequestMapping("/api/audience")
public class HealthController {

    @GetMapping("/health")
    public Result<Map<String, String>> health() {
        return Result.success(Map.of(
                "service", CommonConstants.SERVICE_AUDIENCE,
                "status", "UP"
        ));
    }
}
