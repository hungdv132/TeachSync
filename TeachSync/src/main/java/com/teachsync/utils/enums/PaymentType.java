package com.teachsync.utils.enums;

public enum PaymentType {
    CASH("CASH", "Cash", "Tiền mặt"),
    TRANSFER("TRANSFER", "Transfer", "Chuyển khoản"),
    DENIED("DENIED", "Denied", "Hủy thnah toán");

    private final String stringValue;
    private final String stringValueEng;
    private final String stringValueVie;

    PaymentType(String stringValue, String stringValueEng, String stringValueVie) {
        this.stringValue = stringValue;
        this.stringValueEng = stringValueEng;
        this.stringValueVie = stringValueVie;
    }

    public String getStringValue() {
        return stringValue;
    }

    public String getStringValueEng() {
        return stringValueEng;
    }

    public String getStringValueVie() {
        return stringValueVie;
    }
}
