package com.bntu.master.attendance.monitor.api.model;

public enum SubjectTypeConstant {
    LECTURE(1, "Лекция"),
    PRACTICE(3, "Практика"),
    LAB(2, "Лабораторная"),
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
