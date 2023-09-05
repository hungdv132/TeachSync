package com.teachsync.utils.enums;

public enum StaffType {
    MANAGER("MANAGER"),
    TEACHER("TEACHER"),
    ACCOUNTANT("ACCOUNTANT"),
    EMPLOYEE("EMPLOYEE");

    private final String stringValue;

    StaffType(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}
