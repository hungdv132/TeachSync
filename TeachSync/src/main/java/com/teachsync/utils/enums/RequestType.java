package com.teachsync.utils.enums;

public enum RequestType {
    ENROLL("ENROLL", "Enrollment", "Đăng ký học"),
    CHANGE_CLASS("CHANGE_CLASS", "Change class", "Chuyển lớp"),
    APPLICATION("APPLICATION", "Recruitment application", "Xin việc");

    private final String stringValue;
    private final String stringValueEng;
    private final String stringValueVie;

    RequestType(String stringValue, String stringValueEng, String stringValueVie) {
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
