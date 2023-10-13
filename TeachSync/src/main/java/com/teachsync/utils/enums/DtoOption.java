package com.teachsync.utils.enums;

public enum DtoOption {
    /** Clazz */
    COURSE_SEMESTER,
    /** Clazz */
    CLAZZ_SCHEDULE,
    /** Clazz */
    SESSION_LIST,
    /** Clazz */
    MEMBER_LIST,
    /** Clazz */
    STAFF,
    /** Clazz */
    HOMEWORK_LIST,
    /** Clazz, Course */
    TEST_LIST,

    /** MemberTestRecord */
    CLAZZ_TEST,

    /** ClazzTest */
    TEST,

    /** Test */
    QUESTION_LIST,

    /** Question */
    ANSWER_LIST,

    /** ClazzSchedule, ClazzMember, Request */
    CLAZZ_NAME,
    /** ClazzSchedule, Session */
    ROOM_NAME,

    /** Session */
    ROOM,

    /** ClazzMember, Request */
    CLAZZ,
    /** ClazzMember, Staff, CampaignApplication, Request */
    USER,
    /** ClazzMember, Staff */
    USER_FULL_NAME,

    /** MemberHomeWorkRecord */
    MEMBER,
    /** MemberHomeWorkRecord */
    HOMEWORK,

    /** User */
    ROLE,
    /** User, Center */
    ADDRESS,

    /** Center */
    ROOM_LIST,
    /** Center */
    STAFF_LIST,

    /** Course */
    CLAZZ_LIST,
    /** Course */
    CLAZZ_LIST_ALL,
    /** Course */
    MATERIAL_LIST,
    /** Course */
    CURRENT_PRICE,
    /** Course */
    PRICE_LOG,
    /** Course */
    CERTIFICATE,

    /** Material */
    COURSE_LIST,

    /** CourseSemester */
    COURSE,
    /** CourseSemester */
    COURSE_NAME,
    /** CourseSemester */
    COURSE_ALIAS,
    /** CourseSemester, Staff, RecruitmentCampaign */
    CENTER,
    /** CourseSemester, Staff, RecruitmentCampaign */
    CENTER_NAME,
    /** CourseSemester */
    SEMESTER,
    /** CourseSemester */
    SEMESTER_NAME,
    /** CourseSemester */
    SEMESTER_ALIAS,

    /** RecruitmentCampaign */
    APPLICATION_LIST,

    /** CampaignApplication */
    CAMPAIGN,
    /** CampaignApplication */
    APPLICATION_DETAIL_LIST,

    /** Request */
    REQUESTER,
    /** Request */
    REQUESTER_FULL_NAME,
    /** Request */
    REQUESTER_USERNAME,
    /** Request */
    RESOLVER,
    /** Request */
    RESOLVER_FULL_NAME,
    /** Request */
    RESOLVER_USERNAME,
    /** Request */
    PAYMENT,

    /** Payment */
    REQUEST,
    /** Payment */
    PAYER,

    /** Schedule category */
    SCHEDULE_CAT;
}