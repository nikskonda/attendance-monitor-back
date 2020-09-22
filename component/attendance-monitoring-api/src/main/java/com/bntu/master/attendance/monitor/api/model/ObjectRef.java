package com.bntu.master.attendance.monitor.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ObjectRef {

    private Long id;
    private String qualifier;

    public ObjectRef(Long id) {
        this.id = id;
    }

    public ObjectRef(String qualifier) {
        this.qualifier = qualifier;
    }

    @JsonIgnore
    public boolean isNullId() {
        return id == null;
    }

    @JsonIgnore
    public boolean isNullQualifier() {
        return StringUtils.isBlank(qualifier);
    }

    @JsonIgnore
    public boolean isNullable() {
        return isNullId() && isNullQualifier();
    }

    @JsonIgnore
    public boolean isNullAnyField() {
        return isNullId() || isNullQualifier();
    }

    @JsonIgnore
    public static ObjectRef toObjectRef(Long id) {
        return new ObjectRef(id);
    }

    @JsonIgnore
    public static ObjectRef toObjectRef(String qualifier) {
        return new ObjectRef(qualifier);
    }

    @JsonIgnore
    public static ObjectRef toObjectRef(Long id, String qualifier) {
        return new ObjectRef(id, qualifier);
    }
}
