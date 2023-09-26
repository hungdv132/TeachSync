package com.teachsync.utils.enums;

public enum StaffType {
    MANAGER("MANAGER","Manager","Quản lý"),
    TEACHER("TEACHER","Teacher","Giáo viên"),
    ACCOUNTANT("ACCOUNTANT","Accountant","Kế toán"),
    EMPLOYEE("EMPLOYEE","Employee","Nhân viên"),
    CANDIDATE("CANDIDATE","Candidate","Ứng viên");

    private final String stringValue;
    private final String stringValueEng;
    private final String stringValueVie;

    StaffType(String stringValue, String stringValueEng, String stringValueVie) {
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
