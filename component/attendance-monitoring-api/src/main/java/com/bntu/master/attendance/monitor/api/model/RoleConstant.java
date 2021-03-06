package com.bntu.master.attendance.monitor.api.model;

import java.util.Arrays;
import java.util.Optional;

public enum RoleConstant {

    STUDENT(1L, "STUDENT"),
    PROFESSOR(2L, "PROFESSOR"),
    PARENT(3L, "PARENT"),
    REPORT_VIEW(4L, "REPORT_VIEW"),
    EDITOR(5L, "EDITOR"),
    ;

    private Long id;
    private String role;

    RoleConstant(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    public ObjectRef get() {
        return new ObjectRef(id, role);
    }

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public ObjectRef find(Long toFindId) {
        Optional<RoleConstant> role = Arrays.stream(RoleConstant.values()).filter(r -> r.id.equals(toFindId)).findFirst();
        return role.map(roleConstant -> new ObjectRef(roleConstant.id, roleConstant.role)).orElse(null);
    }

    public ObjectRef find(String toFindRole) {
        Optional<RoleConstant> role = Arrays.stream(RoleConstant.values()).filter(r -> r.role.equals(toFindRole)).findFirst();
        return role.map(roleConstant -> new ObjectRef(roleConstant.id, roleConstant.role)).orElse(null);
    }

    public static RoleConstant findConstant(String toFindRole) {
        Optional<RoleConstant> role = Arrays.stream(RoleConstant.values()).filter(r -> r.role.equals(toFindRole)).findFirst();
        return role.orElse(null);
    }
}
