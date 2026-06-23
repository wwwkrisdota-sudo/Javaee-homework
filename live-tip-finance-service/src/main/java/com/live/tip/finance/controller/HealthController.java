package com.live.tip.finance.controller;

import com.live.tip.common.constant.CommonConstants;
import com.live.tip.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/finance")
public class HealthController {

    @GetMapping("/health")
    public Result<Map<String, String>> health() {
        return Result.success(Map.of(
                "service", CommonConstants.SERVICE_FINANCE,
                "status", "UP"
        ));
    }
}
