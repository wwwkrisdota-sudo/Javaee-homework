package com.live.tip.audience.job;

import com.live.tip.audience.service.FinanceSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FinanceSyncJob {

    private final FinanceSyncService financeSyncService;

    @Scheduled(fixedDelayString = "${audience.finance-sync.interval-ms:60000}")
    public void syncPendingTips() {
        var result = financeSyncService.syncPendingTips();
        if (result.getTotalScanned() > 0) {
            log.info("定时财务同步完成, scanned={}, success={}, failed={}",
                    result.getTotalScanned(), result.getSuccessCount(), result.getFailedCount());
        }
    }
}
