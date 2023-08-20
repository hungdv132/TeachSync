package com.teachsync.utils.enums;

public enum PromotionType {
    PERCENT("PERCENT", "%", "percentage"),
    AMOUNT("AMOUNT", "₫", "currency");

    private final String stringValue;
    private final String displayValue;

    private final String fmtType;

    PromotionType(String stringValue, String displayValue, String fmtType) {
        this.stringValue = stringValue;
        this.displayValue = displayValue;
        this.fmtType = fmtType;
    }

    public String getStringValue() { return stringValue; }

    public String getDisplayValue() { return displayValue; }

    public String getFmtType() { return fmtType; }
}
