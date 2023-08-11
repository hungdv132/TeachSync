package com.teachsync.utils.enums;

public enum ScheduleType {
    MON_WED_FRI("MON_WED_FRI", "Mon, Wed, Fri", "T2, T4, T6"),
    TUE_THU_SAT("TUE_THU_SAT", "Tue, Wed, Fri", "T3, T5, T7"),
    SAT_SUN("SAT_SUN", "Sat, Sun", "T7, CN"),
    CUSTOM("CUSTOM", "Other", "Khác");

    private final String stringValue;
    private final String stringValueEng;
    private final String stringValueVie;

    ScheduleType(String stringValue, String stringValueEng, String stringValueVie) {
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
