package com.live.tip.common.enums;

import lombok.Getter;

/**
 * 观众消费画像等级
 */
@Getter
public enum ProfileLevelEnum {

    HIGH(1, "高消费人群"),
    MEDIUM(2, "中消费人群"),
    LOW(3, "低消费人群");

    private final int code;
    private final String label;

    ProfileLevelEnum(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public static ProfileLevelEnum fromCode(int code) {
        for (ProfileLevelEnum level : values()) {
            if (level.code == code) {
                return level;
            }
        }
        return MEDIUM;
    }
}
