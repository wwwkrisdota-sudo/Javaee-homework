package com.live.tip.common.enums;

import lombok.Getter;

/**
 * 性别枚举
 */
@Getter
public enum GenderEnum {

    UNKNOWN(0, "未知"),
    MALE(1, "男"),
    FEMALE(2, "女");

    private final int code;
    private final String label;

    GenderEnum(int code, String label) {
        this.code = code;
        this.label = label;
    }
}
