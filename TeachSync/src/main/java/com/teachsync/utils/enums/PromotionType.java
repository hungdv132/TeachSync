package com.teachsync.utils.enums;

public enum PromotionType {
    PERCENT("PERCENT", "%"),
    AMOUNT("AMOUNT", "₫");

    private final String stringValue;
    private final String displayValue;

    PromotionType(String stringValue, String displayValue) {
        this.stringValue = stringValue;
        this.displayValue = displayValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
