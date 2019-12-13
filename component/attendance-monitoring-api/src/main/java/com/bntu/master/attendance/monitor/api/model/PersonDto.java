package com.bntu.master.attendance.monitor.api.model;

import lombok.Data;

import java.util.Set;

@Data
public class PersonDto extends ObjectRef {

    private String firstName;
    private String lastName;
    private String patronymic;

    private String email;

    private Set<ObjectRef> roles;

    private ObjectRef group;

}
