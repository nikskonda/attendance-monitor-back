package com.bntu.master.attendance.monitor.api.model.attendance;

public enum AttendanceValue {
    DID_NOT_COME(""),
    TWO_HOUR("2"),
    ONE_HOUR("1"),
    ;

    private String text;

    AttendanceValue(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
