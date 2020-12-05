package com.bntu.master.attendance.monitor.api.model.attendance;

public enum AttendanceValue {
    COME("", 0),
    TWO_HOUR("2", 2),
    ONE_HOUR("1", 1),
    ;

    private String text;

    private Integer hours;

    AttendanceValue(String text, Integer hours) {
        this.text = text;
        this.hours = hours;
    }

    public Integer getHours() {
        return hours;
    }

    public String getText() {
        return text;
    }

    public static AttendanceValue find(String text) {
        if ("0".equals(text)) {
            return COME;
        }
        for (AttendanceValue value : AttendanceValue.values()) {
            if (value.getText().equals(text)) {
                return value;
            }
        }
        return null;
    }

    public static AttendanceValue find(Integer value) {
        for (AttendanceValue att : AttendanceValue.values()) {
            if (att.getHours().equals(value)) {
                return att;
            }
        }
        return null;
    }
}
