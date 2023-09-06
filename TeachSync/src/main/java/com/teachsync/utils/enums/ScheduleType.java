package com.teachsync.utils.enums;

public enum ScheduleType {
    SCHEDULE("SCHEDULE", "SCHEDULE", "Lịch Học"),
    SCHEDULE_REVIEW("SCHEDULE_REVIEW", "SCHEDULE REVIEW", "Lịch Ôn Tập"),
    TEST_SCHEDULE("TEST_SCHEDULE", "TEST SCHEDULE", "Lịch Thi"),
    SCHEDULE_OF_EXTRACURRICULAR_ACTIVITIES("SCHEDULE_OF_EXTRACURRICULAR_ACTIVITIES", "SCHEDULE OF EXTRACURRICULAR ACTIVITIES", "Lịch Sinh Hoạt Ngoại Khoá"),
    CONFERENCE_CALENDAR("CONFERENCE_CALENDAR", "CONFERENCE CALENDAR", "Lịch Hội Thảo");


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
