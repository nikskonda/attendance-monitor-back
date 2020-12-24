package com.bntu.master.attendance.monitor.api.model;

public enum SubjectTypeConstant {
    LECTURE(1, "Лекц."),
    PRACTICE(3, "Практ."),
    LAB(2, "Лаб."),
    ;

    private int sortOrder;
    private String text;

    SubjectTypeConstant(int sortOrder, String text) {
        this.sortOrder = sortOrder;
        this.text = text;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public String getText() {
        return text;
    }
}
