package com.bntu.master.attendance.monitor.api.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public enum GroupVolumeConstant {
    FULL(1L, "FULL"),
    FIRST(2L, "FIRST"),
    SECOND(3L, "SECOND"),
    THIRD(4L, "THIRD"),
    ;

    private Long sortOrder;
    private String name;
    private Long id;

    GroupVolumeConstant(Long id, String name) {
        this.sortOrder = id;
        this.name = name;
        this.id = id;
    }

    public Long getSortOrder() {
        return sortOrder;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public static Optional<GroupVolumeConstant> find(ObjectRef ref) {
        if (ref.isNullAnyField()) {
            return Optional.empty();
        }
        if (ref.isNullQualifier()) {
            return find(ref.getId());
        }
        if (ref.isNullQualifier()) {
            return find(ref.getQualifier());
        }
        return Optional.empty();
    }

    public static Optional<GroupVolumeConstant> find(Long id) {
        if (id != null) {
            for (GroupVolumeConstant gt : GroupVolumeConstant.values()) {
                if (gt.getId().equals(id)) {
                    return Optional.of(gt);
                }
            }
        }
        return Optional.empty();
    }

    public static Optional<GroupVolumeConstant> find(String value) {
        if (StringUtils.isNotBlank(value)) {
            for (GroupVolumeConstant gt : GroupVolumeConstant.values()) {
                if (gt.getName().equalsIgnoreCase(value)) {
                    return Optional.of(gt);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return name;
    }
}
