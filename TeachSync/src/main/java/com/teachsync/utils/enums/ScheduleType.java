package com.teachsync.utils.enums;

public enum ScheduleType {
    MON_WED_FRI("MON_WED_FRI"),
    TUE_THU_SAT("TUE_THU_SAT"),
    SAT_SUN("SAT_SUN"),
    CUSTOM("CUSTOM");

    private final String stringValue;

    ScheduleType(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}
