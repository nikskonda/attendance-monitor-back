package com.bntu.master.attendance.monitor.api.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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
        if (StringUtils.isNotBlank(lastName) && StringUtils.isNotBlank(firstName)) {
            stringBuilder.append(lastName).append(" ").append(firstName);
            if (StringUtils.isNotBlank(patronymic)) {
                stringBuilder.append(" ").append(patronymic);
            }
        }

        return stringBuilder.toString();
    }

    public String getShortName() {
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(lastName) && StringUtils.isNotBlank(firstName)) {

            stringBuilder.append(lastName).append(" ").append(firstName.substring(0, 1)).append(".");
            if (StringUtils.isNotBlank(patronymic)) {
                stringBuilder.append(" ").append(patronymic.substring(0, 1)).append(".");
            }
        }
        return stringBuilder.toString();
    }
}
