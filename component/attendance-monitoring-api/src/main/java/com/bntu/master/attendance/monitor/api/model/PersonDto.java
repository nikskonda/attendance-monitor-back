package com.bntu.master.attendance.monitor.api.model;

import lombok.Data;

import java.util.Set;

@Data
public class PersonDto extends ObjectRef {

    private String firstName;
    private String lastName;
    private String patronymic;

    private String email;

    private String phone;

    private Set<ObjectRef> roles;

    public String getFullName() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(lastName).append(" ").append(firstName).append(" ").append(patronymic);
        return stringBuilder.toString();
    }
}
