package com.live.tip.audience.vo;

import lombok.Data;

@Data
public class FinanceSyncResultVO {

    private int totalScanned;
    private int successCount;
    private int failedCount;
}
