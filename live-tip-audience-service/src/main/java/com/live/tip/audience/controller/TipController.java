package com.live.tip.audience.controller;

import com.live.tip.audience.service.TipService;
import com.live.tip.audience.vo.TipResponse;
import com.live.tip.common.dto.TipRequest;
import com.live.tip.common.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/audience")
@RequiredArgsConstructor
public class TipController {

    private final TipService tipService;

    @PostMapping("/tips")
    public Result<TipResponse> createTip(@Valid @RequestBody TipRequest request) {
        return Result.success(tipService.createTip(request));
    }
}
