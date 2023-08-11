package com.teachsync.utils.enums;

public enum Status {
    PENDING("PENDING"),
    ONGOING("ONGOING"),
    DONE("DONE"),

    APPROVED("APPROVED"),
    DENIED("DENIED"),

    OPENED("OPENED"),
    CLOSED("CLOSED"),

    CREATED("CREATED"),
    UPDATED("UPDATED"),
    DELETED("DELETED");

    private final String stringValue;

    Status(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}
