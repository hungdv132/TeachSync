package com.teachsync.utils.enums;

public enum PromotionType {
    PERCENT("PERCENT"),
    AMOUNT("AMOUNT");

    private final String stringValue;

    PromotionType(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}
