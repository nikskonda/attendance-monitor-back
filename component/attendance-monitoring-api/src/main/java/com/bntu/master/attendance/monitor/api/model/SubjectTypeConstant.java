package com.bntu.master.attendance.monitor.api.model;

public enum SubjectTypeConstant {
    LECTURE(1), PRACTICE(3), LAB(2);

    private int sortOrder;

    SubjectTypeConstant(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getSortOrder() {
        return sortOrder;
    }
}
