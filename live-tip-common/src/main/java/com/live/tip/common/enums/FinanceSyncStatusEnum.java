package com.live.tip.common.enums;

import lombok.Getter;

/**
 * 财务同步状态
 */
@Getter
public enum FinanceSyncStatusEnum {

    PENDING(0, "待同步"),
    SYNCED(1, "已同步"),
    FAILED(2, "同步失败");

    private final int code;
    private final String label;

    FinanceSyncStatusEnum(int code, String label) {
        this.code = code;
        this.label = label;
    }
}
