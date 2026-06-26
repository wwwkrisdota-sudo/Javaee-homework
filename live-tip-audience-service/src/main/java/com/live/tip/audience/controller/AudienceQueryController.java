package com.live.tip.audience.controller;

import com.live.tip.audience.service.FinanceSyncService;
import com.live.tip.audience.service.ProfileService;
import com.live.tip.audience.service.TopTipperService;
import com.live.tip.audience.vo.FinanceSyncResultVO;
import com.live.tip.audience.vo.TopTipperVO;
import com.live.tip.common.dto.AudienceProfileVO;
import com.live.tip.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audience")
@RequiredArgsConstructor
public class AudienceQueryController {

    private final ProfileService profileService;
    private final TopTipperService topTipperService;
    private final FinanceSyncService financeSyncService;

    @GetMapping("/profile/{audienceId}")
    public Result<AudienceProfileVO> getProfile(@PathVariable Long audienceId) {
        return profileService.getProfile(audienceId);
    }

    @GetMapping("/anchors/{anchorId}/top-tippers")
    public Result<List<TopTipperVO>> getTopTippers(
            @PathVariable Long anchorId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(topTipperService.getTopTippers(anchorId, page, size));
    }

    @PostMapping("/tips/sync-finance")
    public Result<FinanceSyncResultVO> syncFinance() {
        return Result.success(financeSyncService.syncPendingTips());
    }
}
