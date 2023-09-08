package com.teachsync.utils.enums;

public enum Status {
    PENDING("PENDING", "Pending", "Đang chờ"),
    ONGOING("ONGOING", "Ongoing", "Đang làm"),
    DONE("DONE", "Done", "Xong"),

    PENDING_PAYMENT("PENDING_PAYMENT", "Pending payment", "Đang chờ thanh toán"),
    AWAIT_CONFIRM("AWAIT_CONFIRM", "Awaiting confirmation", "Đang chờ xác nhận"),
    APPROVED("APPROVED", "Approved", "Chấp nhận"),
    DENIED("DENIED", "Denied", "Từ chối"),

    OPENED("OPENED", "Opened", "Mở"),
    CLOSED("CLOSED", "Closed", "Đóng"),

    ALLOWED_REDO("ALLOWED_REDO", "Allowing redo", "Cho phép làm lại"),
    SUSPENDED("SUSPENDED", "Suspended", "Tạm ngưng"),

    //status of clazz
    CREATED_CLAZZ("CREATED_CLAZZ", "created_clazz", "Đang khởi tạo"),
    DEPLOY_CLAZZ("DEPLOY_CLAZZ", "deploy_clazz", "Đang triển khai"),
    NOT_ENOUGH_CLAZZ("NOT_ENOUGH_CLAZZ", "not_enough_clazz", "Không đủ xếp lớp"),
    FINISH_CLAZZ("FINISH_CLAZZ", "finish_clazz", "Đã hoàn thành"),

    CREATED("CREATED", "created", "Đã tạo"),
    UPDATED("UPDATED", "updated", "Đã cập nhập"),
    DELETED("DELETED", "deleted", "Đã xóa");



    private final String stringValue;
    private final String stringValueEng;
    private final String stringValueVie;

    Status(String stringValue, String stringValueEng, String stringValueVie) {
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
