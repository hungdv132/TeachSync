package com.teachsync.utils.enums;

public enum Gender {
    MALE("MALE", "Male", "Nam"),
    FEMALE("FEMALE", "Female", "Nữ"),
    OTHER("OTHER", "Other", "Khác");

    private final String stringValue;
    private final String stringValueEng;
    private final String stringValueVie;

    Gender(String stringValue, String stringValueEng, String stringValueVie) {
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
